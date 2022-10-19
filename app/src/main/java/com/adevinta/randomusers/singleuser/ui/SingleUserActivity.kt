package com.adevinta.randomusers.singleuser.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.adevinta.randomusers.R
import com.adevinta.randomusers.allusers.model.User
import com.adevinta.randomusers.common.utils.UUID
import com.adevinta.randomusers.common.utils.dateFormatter
import com.adevinta.randomusers.databinding.ActivitySingleUserBinding
import com.adevinta.randomusers.singleuser.viewmodel.SingleUserViewModel
import com.adevinta.randomusers.singleuser.viewmodel.SingleUserViewModelImpl
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel

class SingleUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySingleUserBinding
    private val viewModel: SingleUserViewModel by viewModel<SingleUserViewModelImpl>()
    private var uuid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivitySingleUserBinding.inflate(layoutInflater).apply {
            binding = this
            setContentView(root)
        }
        window.statusBarColor = ContextCompat.getColor(this, R.color.teal_700)
        getExtras()
        getUserInfo()
    }

    private fun getExtras(){
        if(intent.hasExtra(UUID)){
            uuid = intent.extras?.get(UUID).toString()
        }
    }

    private fun getUserInfo() {
        if (uuid != null) {
            with(viewModel) {
                getUserFromDataBase(uuid = uuid ?: "")

                user.observe(this@SingleUserActivity) { result ->
                    binding.bindView(result)
                }

                error.observe(this@SingleUserActivity) { error ->
                    Log.e("SingleUserActivity", "$error")
                }
            }

        }
    }

    private fun ActivitySingleUserBinding.bindView(user: User) {
        with(user) {
            name.text = nameSurname
            phoneText.text = getString(R.string.phone, phone)
            emailText.text = getString(R.string.email, email)
            genderText.text = getString(R.string.gender, gender)
            val location =
                "${location.street.name}, ${location.street.number}, ${location.city}, ${location.state}"
            loacation.text = getString(R.string.location, location)
            registerDateText.text =
                getString(R.string.registration, registerDate.date.dateFormatter())
            Glide.with(binding.root).load(picture).circleCrop()
                .placeholder(R.drawable.ic_user_placeholder).into(binding.picture)
        }
        setupListeners()
    }

    private fun ActivitySingleUserBinding.setupListeners(){
        close.setOnClickListener {
            finish()
        }
    }
}