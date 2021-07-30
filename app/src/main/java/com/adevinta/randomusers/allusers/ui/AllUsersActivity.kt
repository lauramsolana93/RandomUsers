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
import com.adevinta.randomusers.allusers.model.User
import com.adevinta.randomusers.allusers.ui.adapter.AllUsersAdapter
import com.adevinta.randomusers.allusers.viewmodel.AllUsersViewModel
import com.adevinta.randomusers.common.utils.DRAWABLE_RIGHT
import com.adevinta.randomusers.common.utils.Resource
import com.adevinta.randomusers.databinding.ActivityAllUsersBinding
import com.adevinta.randomusers.di.injectModule
import com.adevinta.randomusers.singleuser.ui.SingleUserActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_all_users.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class AllUsersActivity : AppCompatActivity(), AllUsersAdapter.ListItemClickListener {

    private lateinit var binding: ActivityAllUsersBinding
    private val viewModel: AllUsersViewModel by viewModel()
    private lateinit var usersAdapter: AllUsersAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var isLoading: Boolean = false
    private var users = mutableListOf<User>()
    private var page = 0
    private var dbPage = 0
    private lateinit var imm: InputMethodManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllUsersBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        injectModule()
        setUp()
    }

    private fun setUp() {
        checkDatabase()
        setUpRecyclerView()
        setUpSearch()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpSearch() {
        binding.searchIcon.setOnClickListener {
            binding.searchTextView.visibility = VISIBLE
            binding.searchIcon.visibility = GONE
        }
        binding.searchTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                checkSearch(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        binding.searchTextView.setOnTouchListener { _, event ->


            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (binding.searchTextView.right - binding.searchTextView.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                    hideKeyBoard()
                    binding.searchTextView.text.clear()
                    binding.searchTextView.visibility = GONE
                    updateSearchList(users)
                    binding.searchIcon.visibility = VISIBLE
                } else {
                    binding.searchTextView.requestFocus()
                    showKeyBoard()
                }
            }

            return@setOnTouchListener true
        }
    }


    private fun checkSearch(query: String) {
        if (query.isNotBlank()) {
            val list = users.filter { user ->
                val regex = query.toRegex(RegexOption.IGNORE_CASE)
                regex.containsMatchIn(user.email)
                regex.containsMatchIn(user.nameSurname)
            }
            updateSearchList(list)
        }

    }

    private fun checkDatabase() {
        viewModel.getAllUsersFromDataBase(dbPage)

        viewModel.usersDatabase.observe(this, { resultUsers ->
            showLoading()
            resultUsers?.let {
                if (resultUsers.users == users || resultUsers.users.isEmpty()) {
                    getAllUsers()
                } else {
                    this.dbPage = resultUsers.info.page
                    loadRandomUsers(resultUsers.users)
                }
            }
        })
        viewModel.error.observe(this, { error ->
            Log.e("SingleUserActivity", "$error")
        })
    }

    private fun getAllUsers() {
        viewModel.getAllUsersByPage(page)
        viewModel.usersResult.observe(this, { result ->
            when (result) {
                is Resource.Success -> {
                    isLoading = true
                    result.data?.let { resultUsers ->
                        if (resultUsers.users != users) {
                            this.page = resultUsers.info.page
                            saveUsers(resultUsers.users)
                            loadRandomUsers(resultUsers.users)
                        }
                    }
                }
                is Resource.Error -> {
                    isLoading = false
                    showErrorDialog()
                }
                is Resource.Loading -> {
                    showLoading()
                }
            }

        })
    }


    private fun setUpRecyclerView() {
        usersAdapter = AllUsersAdapter(this)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        binding.recyclerView.adapter = usersAdapter

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

    private fun loadRandomUsers(usersList: List<User>) {
        GlobalScope.launch(Dispatchers.Main) {
            delay(2000)
            updateDataList(usersList)
            hideLoading()
        }
    }

    private fun updateSearchList(searchList: List<User>) {
        usersAdapter.submitList(searchList)
    }

    private fun updateDataList(newUsersList: List<User>) {
        val tempList = users.toMutableList()
        tempList.addAll(newUsersList)
        tempList.distinct()
        usersAdapter.submitList(tempList)
        usersAdapter.notifyDataSetChanged()
        users = tempList
    }

    private fun saveUsers(users: List<User>) {
        viewModel.saveUsers(users)
        viewModel.savedInDatabase.observe(this, { result ->
            when (result) {
                is Resource.Success -> {
                    Toast.makeText(this, getString(R.string.add_database), Toast.LENGTH_SHORT)
                        .show()
                }
                is Resource.Error -> {
                    Toast.makeText(
                        this,
                        getString(R.string.problem_add_database),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    //do Nothing
                }
            }
        })
    }

    override fun onItemClick(item: User, position: Int) {
        var intent = Intent(this, SingleUserActivity::class.java)
        intent.putExtra("UUID", item.uuid)
        startActivity(intent)
    }

    override fun onDeleteClick(item: User, position: Int) {
        users.remove(item)
        viewModel.deleteUsers(item)
        usersAdapter.notifyDataSetChanged()
        usersAdapter.notifyItemRangeChanged(position, users.size)
        viewModel.deleteDatabase.observe(this, { result ->
            if (result) {
                Toast.makeText(this, getString(R.string.user_deleted), Toast.LENGTH_SHORT)
                    .show()

            }
        })
        viewModel.error.observe(this, { error ->
            Log.e("SingleUserActivity", " DATABASE: $error")
        })
    }

    //If there is more than one activity that use this create a base activity
    private fun showKeyBoard() {
        imm.showSoftInput(binding.searchTextView, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyBoard() {
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun showLoading() {
        isLoading = true
        binding.loading.loadingView.visibility = VISIBLE
        binding.loading.loadingAnimation.playAnimation()
    }

    private fun hideLoading() {
        isLoading = false
        binding.loading.loadingView.visibility = GONE
        binding.loading.loadingAnimation.cancelAnimation()
    }
}