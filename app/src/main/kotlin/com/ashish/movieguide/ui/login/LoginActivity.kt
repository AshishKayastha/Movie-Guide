package com.ashish.movieguide.ui.login

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.ashish.movieguide.R
import com.ashish.movieguide.di.modules.ActivityModule
import com.ashish.movieguide.di.multibindings.activity.ActivityComponentBuilderHost
import com.ashish.movieguide.ui.base.mvp.MvpActivity
import com.ashish.movieguide.ui.widget.FontButton
import com.ashish.movieguide.ui.widget.FontTextView
import com.ashish.movieguide.utils.TraktConstants.REDIRECT_URI
import com.ashish.movieguide.utils.TraktConstants.TRAKT_CLIENT_ID
import com.ashish.movieguide.utils.TraktConstants.TRAKT_OAUTH_URL
import com.ashish.movieguide.utils.Utils
import com.ashish.movieguide.utils.extensions.applyText
import com.ashish.movieguide.utils.extensions.bindView
import com.ashish.movieguide.utils.extensions.changeViewGroupTextFont
import com.ashish.movieguide.utils.extensions.hide
import com.ashish.movieguide.utils.extensions.hideWithAnimation
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.extensions.show
import com.ashish.movieguide.utils.extensions.showOrHideWithAnimation
import icepick.State
import timber.log.Timber
import java.math.BigInteger
import java.security.SecureRandom

class LoginActivity : MvpActivity<LoginView, LoginPresenter>(), LoginView {

    private val webView: WebView by bindView(R.id.web_view)
    private val connectBtn: FontButton by bindView(R.id.connect_btn)
    private val statusText: FontTextView by bindView(R.id.status_text)
    private val progressBar: ProgressBar by bindView(R.id.progress_bar)

    @JvmField @State val state: String = BigInteger(130, SecureRandom()).toString(32)

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.slide_in_right, R.anim.hold_anim)
        super.onCreate(savedInstanceState)

        toolbar?.changeViewGroupTextFont()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        webView.clearCache(true)
        CookieManager.getInstance().removeAllCookies {}

        webView.settings.javaScriptEnabled = true
        webView.setWebViewClient(LoginWebViewClient())

        connectBtn.setOnClickListener {
            if (Utils.isOnline()) {
                showOrHideConnectBtn(false)
                webView.loadUrl(getAuthUrl())
            } else {
                showMessage(R.string.error_no_internet)
            }
        }
    }

    override fun injectDependencies(builderHost: ActivityComponentBuilderHost) {
        builderHost.getActivityComponentBuilder(LoginActivity::class.java, LoginComponent.Builder::class.java)
                .withModule(ActivityModule(this))
                .build()
                .inject(this)
    }

    override fun getLayoutId() = R.layout.activity_login

    private fun getAuthUrl(): String {
        return TRAKT_OAUTH_URL +
                "?response_type=code" +
                "&client_id=" + TRAKT_CLIENT_ID +
                "&redirect_uri=" + REDIRECT_URI +
                "&state=" + state
    }

    private fun showOrHideConnectBtn(showBtn: Boolean) {
        webView.showOrHideWithAnimation(!showBtn)
        connectBtn.showOrHideWithAnimation(showBtn)
    }

    private fun showStatusText(message: String?, showProgressBar: Boolean = false) {
        statusText.applyText(message)
        progressBar.showOrHideWithAnimation(showProgressBar)
        statusText.showOrHideWithAnimation(message.isNotNullOrEmpty())
    }

    override fun onLoginSuccess() {
        showMessage(R.string.success_trakt_login)
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
        webView.setWebViewClient(null)
        webView.destroy()
        super.onDestroy()
    }

    private inner class LoginWebViewClient : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progressBar.show()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            progressBar.hide()
        }

        @Suppress("OverridingDeprecatedMember")
        override fun onReceivedError(view: WebView, errorCode: Int, description: String?, failingUrl: String?) {
            Timber.d("Failing Url: %s", failingUrl)
            showOrHideConnectBtn(true)
            showStatusText(getString(R.string.error_trakt_login) + "\n\n(" + errorCode + " " + description + ")")
        }

        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            if (getAuthorizationCode(request.url)) return true
            return false
        }
    }

    private fun getAuthorizationCode(uri: Uri?): Boolean {
        Timber.d("getAuthorizationCode: %s", uri.toString())
        if (uri != null && uri.toString().startsWith(REDIRECT_URI)) {
            val code = uri.getQueryParameter("code")
            Timber.d("Code: %s", code)
            val state = uri.getQueryParameter("state")
            Timber.d("State: %s", state)
            exchangeToken(code, state)
            return true
        }
        return false
    }

    private fun exchangeToken(code: String?, state: String?) {
        if (code.isNotNullOrEmpty() && state == this.state) {
            webView.hideWithAnimation()
            showStatusText(getString(R.string.trakt_logging_in), true)
            presenter?.exchangeAccessToken(code!!)
        } else {
            showOrHideConnectBtn(true)
            showStatusText(getString(R.string.error_trakt_login))
        }
    }
}