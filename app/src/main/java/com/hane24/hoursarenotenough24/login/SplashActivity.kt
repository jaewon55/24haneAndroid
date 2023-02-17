package com.hane24.hoursarenotenough24.login

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.hane24.hoursarenotenough24.App
import com.hane24.hoursarenotenough24.MainActivity
import com.hane24.hoursarenotenough24.databinding.ActivitySplashBinding
import com.hane24.hoursarenotenough24.error.NetworkErrorDialog
import com.hane24.hoursarenotenough24.error.NetworkObserver
import com.hane24.hoursarenotenough24.error.NetworkObserverImpl

enum class State {
    UNKNOWN_ERROR,
    SUCCESS,
    LOGIN_FAIL,
    SERVER_FAIL,
}

class SplashActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    private val viewModel by lazy { ViewModelProvider(this)[SplashViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setStatusAndNavigationBar()
        checkNetworkState(NetworkObserverImpl().isConnected())
        checkLogin()
        observeLoginState()
    }

    fun checkLogin() {
        viewModel.checkLogin()
    }

    private fun checkNetworkState(networkState: Boolean) {
        val onClick = DialogInterface.OnClickListener { _, _ ->
            checkNetworkState(NetworkObserverImpl().isConnected())
            checkLogin()
        }

        when (networkState) {
            false -> NetworkErrorDialog.showNetworkErrorDialog(supportFragmentManager, onClick)
            true -> return
        }
    }

    private fun observeLoginState() {
        viewModel.state.observe(this) { loginState ->
            loginState?.let { checkLoginState(it) }
        }
    }

    private fun checkLoginState(state: State) {
        when (state) {
            State.SUCCESS -> {
                goToMain()
            }

            State.LOGIN_FAIL -> {
                goToLogin(state)
            }

            else -> return
        }
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)

        startActivity(intent).also { finish() }
    }

    private fun goToLogin(state: State) {
        val intent = Intent(this, LoginActivity::class.java)
            .putExtra("loginState", state)

        startActivity(intent).also { finish() }
    }

    private fun setStatusAndNavigationBar() {
        val controller = WindowInsetsControllerCompat(window, window.decorView)

        controller.isAppearanceLightStatusBars = true
        controller.isAppearanceLightNavigationBars = true
    }
}