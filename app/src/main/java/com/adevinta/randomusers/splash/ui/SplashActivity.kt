package com.adevinta.randomusers.splash.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.adevinta.randomusers.R
import com.adevinta.randomusers.allusers.ui.AllUsersActivity
import com.adevinta.randomusers.common.utils.Resource
import com.adevinta.randomusers.databinding.ActivitySplashBinding
import com.adevinta.randomusers.splash.viewmodel.SplashViewModel
import com.adevinta.randomusers.splash.viewmodel.SplashViewModelImpl
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModel<SplashViewModelImpl>()
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivitySplashBinding.inflate(layoutInflater).apply {
            binding = this
            setContentView(root)
        }
        window.statusBarColor = ContextCompat.getColor(this, R.color.teal_700)
        initSetUp()
    }

    private fun initSetUp() {
        checkNetworkConnection()
    }

    private fun checkNetworkConnection() {
        with(viewModel) {
            checkNetworkConnection(this@SplashActivity)
            networkConnection.observe(this@SplashActivity) { result ->
                when (result) {
                    is Resource.Success -> {
                        binding.loadingUsers.cancelAnimation()
                        navigateToAllUsers()

                    }
                    is Resource.Error -> {
                        MaterialAlertDialogBuilder(this@SplashActivity)
                            .setTitle(getString(R.string.network_error_title))
                            .setMessage(getString(R.string.network_error_text))
                            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                                finish()
                            }
                            .setOnDismissListener { viewModel.checkNetworkConnection(this@SplashActivity) }
                            .show()
                    }
                    is Resource.Loading -> {
                        binding.loadingUsers.playAnimation()
                    }
                }
            }
        }

    }

    private fun navigateToAllUsers() {
        startActivity(Intent(this, AllUsersActivity::class.java))
        finish()
    }
}