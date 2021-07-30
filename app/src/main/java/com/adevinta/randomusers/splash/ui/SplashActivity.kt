package com.adevinta.randomusers.splash.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.adevinta.randomusers.R
import com.adevinta.randomusers.allusers.ui.AllUsersActivity
import com.adevinta.randomusers.common.utils.Resource
import com.adevinta.randomusers.databinding.ActivitySplashBinding
import com.adevinta.randomusers.di.injectModule
import com.adevinta.randomusers.splash.viewmodel.SplashViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModel()
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        window.statusBarColor = ContextCompat.getColor(this, R.color.teal_700)
        injectModule()
        initSetUp()
    }

    private fun initSetUp() {
        checkNetworkConnection()
    }

    private fun checkNetworkConnection() {
        viewModel.checkNetworkConnection(this)
        viewModel.networkConnection.observe(this, { result ->
            when (result) {
                is Resource.Success -> {
                    binding.loadingUsers.cancelAnimation()
                    navigateToAllUsers()

                }
                is Resource.Error -> {
                    MaterialAlertDialogBuilder(this)
                        .setTitle(getString(R.string.network_error_title))
                        .setMessage(getString(R.string.network_error_text))
                        .setPositiveButton(getString(R.string.try_again)) { _, _ ->
                            viewModel.checkNetworkConnection(this)
                        }
                        .setNegativeButton("CONTINUE") { _, _ ->
                            navigateToAllUsers()
                        }
                        .setOnDismissListener { viewModel.checkNetworkConnection(this) }
                        .show()
                }
                is Resource.Loading -> {
                    binding.loadingUsers.playAnimation()
                }
            }
        })
    }

    private fun navigateToAllUsers() {
        startActivity(Intent(this, AllUsersActivity::class.java))
        finish()


    }
}