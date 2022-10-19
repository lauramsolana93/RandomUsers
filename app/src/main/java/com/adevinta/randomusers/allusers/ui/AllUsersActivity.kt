package com.adevinta.randomusers.allusers.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adevinta.randomusers.R
import com.adevinta.randomusers.common.utils.DRAWABLE_RIGHT
import com.adevinta.randomusers.allusers.model.User
import com.adevinta.randomusers.allusers.ui.adapter.AllUsersAdapter
import com.adevinta.randomusers.allusers.viewmodel.AllUsersViewModel
import com.adevinta.randomusers.allusers.viewmodel.AllUsersViewModelImpl
import com.adevinta.randomusers.common.utils.*
import com.adevinta.randomusers.databinding.ActivityAllUsersBinding
import com.adevinta.randomusers.singleuser.ui.SingleUserActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class AllUsersActivity : AppCompatActivity(), AllUsersAdapter.ListItemClickListener {

    private lateinit var binding: ActivityAllUsersBinding
    private val viewModel: AllUsersViewModel by viewModel<AllUsersViewModelImpl>()
    private lateinit var usersAdapter: AllUsersAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var isLoading: Boolean = false
    private var users = mutableListOf<User>()
    private var page = 0
    private var dbPage = 0
    private lateinit var imm: InputMethodManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityAllUsersBinding.inflate(layoutInflater).apply {
            binding = this
            setContentView(root)
        }
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        setUp()
    }

    private fun setUp() {
        checkDatabase()
        setUpRecyclerView()
        binding.setUpSearch()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun ActivityAllUsersBinding.setUpSearch() {
        searchIcon.setOnClickListener {
            searchTextView.visibility = VISIBLE
            searchIcon.visibility = GONE
        }
        searchTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                checkSearch(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        with(searchTextView) {
            setOnTouchListener { _, event ->

                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= (right - this.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                        hideKeyBoard()
                        text.clear()
                        visibility = GONE
                        updateSearchList(users)
                        searchIcon.visibility = VISIBLE
                    } else {
                        requestFocus()
                        showKeyBoard()
                    }
                }
                return@setOnTouchListener true
            }
        }
    }


    private fun checkSearch(query: String) {
        if (query.isNotBlank()) {
            val list = users.filter { user ->
                query.toRegex(RegexOption.IGNORE_CASE).run {
                    containsMatchIn(user.email)
                    containsMatchIn(user.nameSurname)
                }
            }
            updateSearchList(list)
        }
    }

    private fun checkDatabase() {
        with(viewModel) {
            getAllUsersFromDataBase(dbPage)

            usersDatabase.observe(this@AllUsersActivity) { resultUsers ->
                hasToShowLoading(isLoading = true)
                resultUsers?.let {
                    if (resultUsers.users == users || resultUsers.users.isEmpty()) {
                        getAllUsers()
                    } else {
                        this@AllUsersActivity.dbPage = resultUsers.info.page
                        loadRandomUsers(resultUsers.users)
                    }
                }
            }
            error.observe(this@AllUsersActivity) { error ->
                Log.e("SingleUserActivity", "$error")
            }
        }

    }

    private fun getAllUsers() {
        with(viewModel) {
            getAllUsersByPage(page)
            usersResult.observe(this@AllUsersActivity) { result ->
                when (result) {
                    is Resource.Success -> {
                        isLoading = true
                        result.data?.let { resultUsers ->
                            if (resultUsers.users != users) {
                                this@AllUsersActivity.page = resultUsers.info.page
                                this@AllUsersActivity.saveUsers(resultUsers.users)
                                loadRandomUsers(resultUsers.users)
                            }
                        }
                    }
                    is Resource.Error -> {
                        isLoading = false
                        showErrorDialog()
                    }
                    is Resource.Loading -> {
                        hasToShowLoading(isLoading = true)
                    }
                }
            }
        }
    }


    private fun setUpRecyclerView() {
        usersAdapter = AllUsersAdapter(this)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        with(binding.recyclerView) {
            layoutManager = this@AllUsersActivity.layoutManager
            itemAnimator = DefaultItemAnimator()
            adapter = usersAdapter
        }

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!isLoading) {
                    if (layoutManager.findLastVisibleItemPosition() == users.size - 1) {
                        viewModel.getAllUsersFromDataBase(dbPage)
                        isLoading = true
                    }
                }
            }
        })

        usersAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                layoutManager.scrollToPosition(positionStart)
            }
        })
    }

    private fun showErrorDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.no_users_error_title))
            .setMessage(getString(R.string.no_users_error_text))
            .setPositiveButton(getString(R.string.try_again)) { _, _ ->
                viewModel.getAllUsersByPage(0)
            }
            .setOnDismissListener { viewModel.getAllUsersByPage(0) }
            .show()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadRandomUsers(usersList: List<User>) {
        GlobalScope.launch(Dispatchers.Main) {
            delay(2000)
            updateDataList(usersList)
            hasToShowLoading(isLoading = false)
        }
    }

    private fun updateSearchList(searchList: List<User>) {
        usersAdapter.submitList(searchList)
    }

    private fun updateDataList(newUsersList: List<User>) {
        val tempList = users.toMutableList().apply {
            addAll(newUsersList)
            distinct()
        }
        with(usersAdapter){
            submitList(tempList)
            notifyDataSetChanged()
        }
        users = tempList
    }

    private fun saveUsers(users: List<User>) {
        with(viewModel) {
            saveUsers(users)
            savedInDatabase.observe(this@AllUsersActivity) { result ->
                when (result) {
                    is Resource.Success -> {
                        Toast.makeText(this@AllUsersActivity, getString(R.string.add_database), Toast.LENGTH_SHORT)
                            .show()
                    }
                    is Resource.Error -> {
                        Toast.makeText(
                            this@AllUsersActivity,
                            getString(R.string.problem_add_database),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        //do Nothing
                    }
                }
            }
        }

    }

    override fun onItemClick(item: User, position: Int) {
        Intent(this, SingleUserActivity::class.java).apply {
            putExtra("UUID", item.uuid)
            startActivity(this)
        }
    }

    override fun onDeleteClick(item: User, position: Int) {
        users.remove(item)
        viewModel.deleteUsers(item)
        with(usersAdapter){
            notifyDataSetChanged()
            notifyItemRangeChanged(position, users.size)
        }

        with(viewModel) {
            deleteDatabase.observe(this@AllUsersActivity) { result ->
                if (result) {
                    Toast.makeText(this@AllUsersActivity, getString(R.string.user_deleted), Toast.LENGTH_SHORT)
                        .show()

                }
            }
            error.observe(this@AllUsersActivity) { error ->
                Log.e("SingleUserActivity", " DATABASE: $error")
            }
        }

    }

    //If there is more than one activity that use this create a base activity
    private fun showKeyBoard() {
        imm.showSoftInput(binding.searchTextView, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyBoard() {
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun hasToShowLoading(isLoading: Boolean) {
        with(binding.loading) {
            loadingView.visibleOrGone(visible = isLoading)
            with(loadingAnimation) {
                if(isLoading) playAnimation()
                else cancelAnimation()
            }
        }
    }
}