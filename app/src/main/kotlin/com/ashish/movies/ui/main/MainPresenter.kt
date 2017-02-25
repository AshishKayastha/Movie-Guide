package com.ashish.movies.ui.main

import com.ashish.movies.R
import com.ashish.movies.data.interactors.AuthInteractor
import com.ashish.movies.data.models.RequestToken
import com.ashish.movies.ui.base.mvp.RxPresenter
import com.ashish.movies.utils.Constants.VALIDATE_TMDB_REQUEST_TOKEN_URL
import com.ashish.movies.utils.extensions.isNotNullOrEmpty
import com.ashish.movies.utils.schedulers.BaseSchedulerProvider
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashish on Jan 28.
 */
class MainPresenter @Inject constructor(
        private val authInteractor: AuthInteractor,
        schedulerProvider: BaseSchedulerProvider
) : RxPresenter<MainView>(schedulerProvider) {

    private var tmdbRequestToken: String? = null

    fun createRequestToken() {
        addDisposable(authInteractor.createRequestToken()
                .doOnNext { tmdbRequestToken = it.requestToken }
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { getView()?.showProgressDialog(R.string.logging_in) }
                .subscribe({ onRequestTokenSuccess(it) }, { t -> onRequestTokenError(t) }))
    }

    private fun onRequestTokenSuccess(requestToken: RequestToken) {
        if (requestToken.success && tmdbRequestToken.isNotNullOrEmpty()) {
            getView()?.validateRequestToken(VALIDATE_TMDB_REQUEST_TOKEN_URL + tmdbRequestToken)
        } else {
            onRequestTokenError(null)
        }
    }

    private fun onRequestTokenError(t: Throwable?) {
        t?.let { Timber.e(t) }
        getView()?.apply {
            hideProgressDialog()
            showMessage(R.string.error_tmdb_login)
        }
    }

    fun createSession() {
        if (tmdbRequestToken.isNotNullOrEmpty()) {
            addDisposable(authInteractor.createUserSession(tmdbRequestToken!!)
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ onCreateSessionSuccess() }, { t -> onCreateSessionError(t) }))
        }
    }

    private fun onCreateSessionSuccess() {
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