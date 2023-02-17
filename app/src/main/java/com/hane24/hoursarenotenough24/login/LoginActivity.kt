package com.hane24.hoursarenotenough24.login

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import com.hane24.hoursarenotenough24.BuildConfig
import com.hane24.hoursarenotenough24.MainActivity
import com.hane24.hoursarenotenough24.utils.SharedPreferenceUtils
import com.hane24.hoursarenotenough24.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val cookieManager: CookieManager = CookieManager.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setStatusAndNavigationBar()

        val state = intent.getSerializableExtra("loginState") as State

        val loginUri: Uri = createLoginUri()

        cookieManager.setAcceptCookie(true)
        cookieManager.setAcceptThirdPartyCookies(binding.loginWebView, true)
        binding.loginWebView.webViewClient = CustomWebViewClient()
        binding.loginWebView.settings.javaScriptEnabled = true
        binding.loginWebView.loadUrl(loginUri.toString())
    }

    private fun setStatusAndNavigationBar() {
        val controller = WindowInsetsControllerCompat(window, window.decorView)

        controller.isAppearanceLightStatusBars = true
        controller.isAppearanceLightNavigationBars = true
    }

    private fun createLoginUri() =
        Uri.Builder().scheme("https").authority(BuildConfig.LOGIN_URL)
            .appendPath("user")
            .appendPath("login")
            .appendPath("42")
            .appendQueryParameter("redirect", "app://hane42")
            .build()

    inner class CustomWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (request?.url.toString() == "app://hane42" || request?.url.toString() == "https://profile.intra.42.fr/") {
                return try {
                    redirectToMain()
                    true
                } catch (e: java.lang.Exception) {
                    false
                }
            }
            return false
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            binding.loadingLayout.visibility = View.VISIBLE
            binding.loginWebView.visibility = View.GONE
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            binding.loadingLayout.visibility = View.GONE
            binding.loginWebView.visibility = View.VISIBLE
        }

        private fun redirectToMain() {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent).also {
                val accessToken = parseAccessToken()
                SharedPreferenceUtils.saveAccessToken(accessToken)
                cookieManager.removeAllCookies { }
                this@LoginActivity.finish()
            }
        }

        private fun parseAccessToken(): String =
            cookieManager.getCookie("https://api.24hoursarenotenough.42seoul.kr/user/login/callback/42")
                .substringAfter(' ').split('=')[1]
    }
}