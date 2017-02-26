package com.ashish.movieguide.ui.main

import com.ashish.movieguide.R
import com.ashish.movieguide.data.interactors.AuthInteractor
import com.ashish.movieguide.data.models.RequestToken
import com.ashish.movieguide.di.scopes.ActivityScope
import com.ashish.movieguide.ui.base.mvp.RxPresenter
import com.ashish.movieguide.utils.Constants.VALIDATE_TMDB_REQUEST_TOKEN_URL
import com.ashish.movieguide.utils.Logger
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import javax.inject.Inject

/**
 * Created by Ashish on Jan 28.
 */
@ActivityScope
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
        t?.let { Logger.e(t) }
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
        Logger.e(t)
        getView()?.apply {
            hideProgressDialog()
            showMessage(R.string.error_tmdb_login)
        }
    }
}