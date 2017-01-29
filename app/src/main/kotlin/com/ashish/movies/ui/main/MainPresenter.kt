package com.ashish.movies.ui.main

import com.ashish.movies.R
import com.ashish.movies.data.api.AuthApi
import com.ashish.movies.data.models.Account
import com.ashish.movies.data.models.RequestToken
import com.ashish.movies.data.preferences.PreferenceHelper
import com.ashish.movies.ui.base.mvp.RxPresenter
import com.ashish.movies.utils.extensions.isNotNullOrEmpty
import com.ashish.movies.utils.extensions.observeOnMainThread
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashish on Jan 28.
 */
class MainPresenter @Inject constructor(private val authApi: AuthApi, private val preferenceHelper: PreferenceHelper)
    : RxPresenter<MainView>() {

    private var isChromeTabLaunched = false
    private var tmdbRequestToken: String? = null

    fun createRequestToken() {
        getView()?.showProgressDialog(R.string.logging_in)

        addDisposable(authApi.createRequestToken()
                .doOnNext { tmdbRequestToken = it.requestToken }
                .observeOnMainThread()
                .subscribe({ onRequestTokenSuccess(it) }, { t -> onRequestTokenError(t) }))
    }

    private fun onRequestTokenSuccess(requestToken: RequestToken) {
        if (requestToken.success && tmdbRequestToken.isNotNullOrEmpty()) {
            isChromeTabLaunched = true
            getView()?.validateRequestToken("https://www.themoviedb.org/authenticate/" + tmdbRequestToken)
        } else {
            getView()?.apply {
                hideProgressDialog()
            }
        }
    }

    private fun onRequestTokenError(t: Throwable) {
        Timber.e(t)
        getView()?.apply {
            hideProgressDialog()
            showMessage(R.string.error_tmdb_login)
        }
    }

    fun createSession() {
        if (isChromeTabLaunched && tmdbRequestToken.isNotNullOrEmpty()) {
            addDisposable(authApi.createSession(tmdbRequestToken!!)
                    .doOnError { onCreateSessionError(it) }
                    .filter { it.success }
                    .doOnNext { preferenceHelper.setSessionId(it.sessionId) }
                    .flatMap { authApi.getUserAccount(it.sessionId!!) }
                    .observeOnMainThread()
                    .subscribe({ account -> onCreateSessionSuccess(account) }, { t -> onCreateSessionError(t) }))
        }
    }

    private fun onCreateSessionSuccess(account: Account?) {
        account?.apply {
            with(preferenceHelper) {
                setName(name)
                setUserName(username)
                setGravatarHash(avatar?.gravatar?.hash)
            }
        }

        isChromeTabLaunched = false
        getView()?.apply {
            hideProgressDialog()
            onLoginSuccess()
        }
    }

    private fun onCreateSessionError(t: Throwable) {
        Timber.e(t)
        getView()?.apply {
            hideProgressDialog()
            showMessage(R.string.error_tmdb_login)
        }
    }
}