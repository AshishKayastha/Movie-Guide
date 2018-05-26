package com.ashish.movieguide.ui.login

import com.ashish.movieguide.R
import com.ashish.movieguide.data.network.api.trakt.TraktAuthApi
import com.ashish.movieguide.data.network.api.trakt.UserApi
import com.ashish.movieguide.data.network.entities.trakt.Settings
import com.ashish.movieguide.data.network.entities.trakt.TokenRequest
import com.ashish.movieguide.data.network.entities.trakt.TraktToken
import com.ashish.movieguide.data.preferences.PreferenceHelper
import com.ashish.movieguide.ui.base.mvp.RxPresenter
import com.ashish.movieguide.utils.TraktConstants.GRANT_TYPE_AUTHORIZATION_CODE
import com.ashish.movieguide.utils.TraktConstants.REDIRECT_URI
import com.ashish.movieguide.utils.TraktConstants.TRAKT_CLIENT_ID
import com.ashish.movieguide.utils.TraktConstants.TRAKT_CLIENT_SECRET
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class LoginPresenter @Inject constructor(
        private val traktAuthApi: TraktAuthApi,
        private val userApi: UserApi,
        private val preferenceHelper: PreferenceHelper,
        schedulerProvider: BaseSchedulerProvider
) : RxPresenter<LoginView>(schedulerProvider) {

    fun exchangeAccessToken(code: String) {
        val tokenRequest = TokenRequest(
                code,
                TRAKT_CLIENT_ID,
                TRAKT_CLIENT_SECRET,
                REDIRECT_URI,
                GRANT_TYPE_AUTHORIZATION_CODE
        )

        addDisposable(traktAuthApi.getAccessToken(tokenRequest)
                .doOnSuccess { saveAccessToken(it) }
                .flatMap { userApi.getUserSettings() }
                .observeOn(schedulerProvider.ui())
                .subscribe({ onLoginSuccess(it) }, { handleError(it) }))
    }

    private fun saveAccessToken(traktToken: TraktToken) {
        preferenceHelper.apply {
            setAccessToken(traktToken.accessToken)
            setRefreshToken(traktToken.refreshToken)
        }
    }

    private fun onLoginSuccess(settings: Settings) {
        preferenceHelper.saveUserProfile(settings.user)
        preferenceHelper.setCoverImageUrl(settings.account?.coverImage)
        view?.onLoginSuccess()
    }

    private fun handleError(t: Throwable) {
        Timber.e(t)
        preferenceHelper.apply {
            setAccessToken(null)
            setRefreshToken(null)
        }

        view?.run {
            if (t is IOException) {
                showMessage(R.string.error_no_internet)
            } else {
                onLoginError()
            }
        }
    }
}