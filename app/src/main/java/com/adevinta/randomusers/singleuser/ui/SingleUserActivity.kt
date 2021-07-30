package com.adevinta.randomusers.singleuser.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.adevinta.randomusers.R
import com.adevinta.randomusers.allusers.model.User
import com.adevinta.randomusers.common.utils.UUID
import com.adevinta.randomusers.common.utils.dateFormater
import com.adevinta.randomusers.databinding.ActivitySingleUserBinding
import com.adevinta.randomusers.di.injectModule
import com.adevinta.randomusers.singleuser.viewmodel.SingleUserViewModel
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel

class SingleUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySingleUserBinding
    private val viewModel: SingleUserViewModel by viewModel()
    private var uuid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        injectModule()
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
            viewModel.getUserFromDataBase(uuid = uuid ?: "")

            viewModel.user.observe(this, { result ->
                bindView(result)
            })

            viewModel.error.observe(this, { error ->
                Log.e("SingleUserActivity", "$error")
            })
        }
    }

    private fun bindView(user: User) {
        binding.name.text = user.nameSurname
        binding.phone.text = getString(R.string.phone, user.phone)
        binding.email.text = getString(R.string.email, user.email)
        binding.gender.text = getString(R.string.gender, user.gender)
        var location =
            "${user.location.street.name}, ${user.location.street.number}, ${user.location.city}, ${user.location.state}"
        binding.loacation.text = getString(R.string.location, location)
        binding.registerDate.text =
            getString(R.string.registration, user.registerDate.date.dateFormater())
        Glide.with(binding.root).load(user.picture).circleCrop()
            .placeholder(R.drawable.ic_user_placeholder).into(binding.picture)

        setupListeners()
    }

    private fun setupListeners(){
        binding.close.setOnClickListener {
            finish()
        }
    }


}