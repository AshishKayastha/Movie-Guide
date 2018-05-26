package com.ashish.movieguide.ui.login

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import com.ashish.movieguide.R
import com.ashish.movieguide.ui.base.mvp.MvpActivity
import com.ashish.movieguide.utils.TraktConstants.REDIRECT_URI
import com.ashish.movieguide.utils.TraktConstants.TRAKT_CLIENT_ID
import com.ashish.movieguide.utils.Utils
import com.ashish.movieguide.utils.extensions.applyText
import com.ashish.movieguide.utils.extensions.changeViewGroupTextFont
import com.ashish.movieguide.utils.extensions.hide
import com.ashish.movieguide.utils.extensions.hideWithAnimation
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.extensions.setVisibility
import com.ashish.movieguide.utils.extensions.show
import com.ashish.movieguide.utils.extensions.showOrHideWithAnimation
import com.evernote.android.state.State
import kotlinx.android.synthetic.main.activity_login.*
import java.math.BigInteger
import java.security.SecureRandom
import javax.inject.Inject

class LoginActivity : MvpActivity<LoginView, LoginPresenter>(), LoginView {

    @Inject lateinit var loginPresenter: LoginPresenter

    @State
    var state: String = BigInteger(130, SecureRandom()).toString(32)

    private val loginWebClient: WebViewClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            webProgressBar.show()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            webProgressBar.hide()
        }

        @Suppress("OverridingDeprecatedMember")
        override fun onReceivedError(view: WebView, errorCode: Int, description: String?, failingUrl: String?) {
            showOrHideConnectBtn(true)
            showStatusText(getString(R.string.error_trakt_login) + "\n\n(" + errorCode + " " + description + ")")
        }

        @Suppress("OverridingDeprecatedMember")
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            if (url != null && url.startsWith(REDIRECT_URI)) {
                val uri = Uri.parse(url)
                val code = uri.getQueryParameter("code")
                val state = uri.getQueryParameter("state")
                exchangeToken(code, state)
                return true
            }

            return false
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.slide_in_right, R.anim.hold_anim)
        super.onCreate(savedInstanceState)

        toolbar?.changeViewGroupTextFont()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        webView.clearCache(true)
        CookieManager.getInstance().removeAllCookies {}

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = loginWebClient

        connectBtn.setOnClickListener {
            if (Utils.isOnline()) {
                showOrHideConnectBtn(false)
                webView.loadUrl(getAuthUrl())
            } else {
                showMessage(R.string.error_no_internet)
            }
        }
    }

    override fun getLayoutId() = R.layout.activity_login

    override fun providePresenter(): LoginPresenter = loginPresenter

    private fun getAuthUrl(): String {
        return "https://trakt.tv/oauth/authorize" +
                "?response_type=code" +
                "&client_id=" + TRAKT_CLIENT_ID +
                "&redirect_uri=" + REDIRECT_URI +
                "&state=" + state
    }

    private fun exchangeToken(code: String?, returnedState: String?) {
        if (code.isNotNullOrEmpty() && returnedState == state) {
            webView.hideWithAnimation()
            loginPresenter.exchangeAccessToken(code!!)
            showStatusText(getString(R.string.trakt_logging_in), true)
        } else {
            showOrHideConnectBtn(true)
            showStatusText(getString(R.string.error_trakt_login))
        }
    }

    private fun showOrHideConnectBtn(showBtn: Boolean) {
        webView.showOrHideWithAnimation(!showBtn)
        connectBtn.showOrHideWithAnimation(showBtn)
        joinTraktText.showOrHideWithAnimation(showBtn)
    }

    private fun showStatusText(message: String?, showProgressBar: Boolean = false) {
        statusText.applyText(message)
        progressBar.setVisibility(showProgressBar)
        statusText.showOrHideWithAnimation(message.isNotNullOrEmpty())
    }

    override fun onLoginSuccess() {
        setResult(Activity.RESULT_OK)
        showToastMessage(R.string.success_trakt_login)
        finish()
    }

    override fun onLoginError() {
        showMessage(R.string.error_trakt_login)
    }

    override fun finishAfterTransition() {
        super.finishAfterTransition()
        overridePendingTransition(R.anim.hold_anim, R.anim.slide_out_right)
    }

    override fun onDestroy() {
        webView.webViewClient = null
        webView.destroy()
        super.onDestroy()
    }
}