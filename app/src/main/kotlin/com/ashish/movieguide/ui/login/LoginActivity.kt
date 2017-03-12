package com.ashish.movieguide.ui.login

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.webkit.CookieManager
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
import com.ashish.movieguide.utils.Utils
import com.ashish.movieguide.utils.extensions.applyText
import com.ashish.movieguide.utils.extensions.bindView
import com.ashish.movieguide.utils.extensions.changeViewGroupTextFont
import com.ashish.movieguide.utils.extensions.hide
import com.ashish.movieguide.utils.extensions.hideWithAnimation
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.extensions.setVisibility
import com.ashish.movieguide.utils.extensions.show
import com.ashish.movieguide.utils.extensions.showOrHideWithAnimation
import icepick.State
import java.math.BigInteger
import java.security.SecureRandom

class LoginActivity : MvpActivity<LoginView, LoginPresenter>(), LoginView {

    private val webView: WebView by bindView(R.id.web_view)
    private val connectBtn: FontButton by bindView(R.id.connect_btn)
    private val statusText: FontTextView by bindView(R.id.status_text)
    private val progressBar: ProgressBar by bindView(R.id.progress_bar)
    private val webProgressBar: ProgressBar by bindView(R.id.web_progress_bar)

    @JvmField @State var state: String = BigInteger(130, SecureRandom()).toString(32)

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
        return "https://trakt.tv/oauth/authorize" +
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
        webView.setWebViewClient(null)
        webView.destroy()
        super.onDestroy()
    }

    private inner class LoginWebViewClient : WebViewClient() {
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
                return getAuthorizationCode(Uri.parse(url))
            }

            return false
        }
    }

    private fun getAuthorizationCode(uri: Uri): Boolean {
        val code = uri.getQueryParameter("code")
        val state = uri.getQueryParameter("state")
        exchangeToken(code, state)
        return true
    }

    private fun exchangeToken(code: String?, state: String?) {
        if (code.isNotNullOrEmpty() && state == this.state) {
            webView.hideWithAnimation()
            presenter?.exchangeAccessToken(code!!)
            showStatusText(getString(R.string.trakt_logging_in), true)
        } else {
            showOrHideConnectBtn(true)
            showStatusText(getString(R.string.error_trakt_login))
        }
    }
}