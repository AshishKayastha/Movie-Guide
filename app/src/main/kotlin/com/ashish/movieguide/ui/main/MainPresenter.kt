package com.ashish.movieguide.ui.main

import com.ashish.movieguide.R
import com.ashish.movieguide.data.interactors.AuthInteractor
import com.ashish.movieguide.data.models.tmdb.RequestToken
import com.ashish.movieguide.di.scopes.ActivityScope
import com.ashish.movieguide.ui.base.mvp.RxPresenter
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashish on Jan 28.
 */
@ActivityScope
class MainPresenter @Inject constructor(
        private val authInteractor: AuthInteractor,
        schedulerProvider: BaseSchedulerProvider
) : RxPresenter<MainView>(schedulerProvider) {

    companion object {
        private const val VALIDATE_TMDB_REQUEST_TOKEN_URL = "https://www.themoviedb.org/authenticate/"
    }

    private var tmdbRequestToken: String? = null

    fun createRequestToken() {
        addDisposable(authInteractor.createRequestToken()
                .doOnSuccess { tmdbRequestToken = it.requestToken }
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { getView()?.showProgressDialog(R.string.logging_in) }
                .subscribe({ handleRequestTokenResult(it) }, { onRequestTokenError(it) }))
    }

    private fun handleRequestTokenResult(requestToken: RequestToken) {
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
                    .doOnSubscribe { getView()?.showProgressDialog(R.string.creating_session) }
                    .doFinally { getView()?.hideProgressDialog() }
                    .subscribe({ getView()?.onLoginSuccess() }, { onCreateSessionError(it) }))
        }
    }

    private fun onCreateSessionError(t: Throwable) {
        Timber.e(t)
        getView()?.showMessage(R.string.error_tmdb_login)
    }
}