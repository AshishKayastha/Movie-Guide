package com.ashish.movies.data.interactors

import com.ashish.movies.data.api.AuthApi
import com.ashish.movies.data.models.Account
import com.ashish.movies.data.models.Favorite
import com.ashish.movies.data.models.RequestToken
import com.ashish.movies.data.preferences.PreferenceHelper
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ashish on Jan 29.
 */
@Singleton
class AuthInteractor @Inject constructor(
        private val authApi: AuthApi,
        private val preferenceHelper: PreferenceHelper
) {

    fun createRequestToken(): Observable<RequestToken> {
        return authApi.createRequestToken()
    }

    fun createUserSession(tmdbRequestToken: String): Observable<Account> {
        return authApi.createSession(tmdbRequestToken)
                .filter { it.success }
                .doOnNext { preferenceHelper.setSessionId(it.sessionId) }
                .flatMap { authApi.getUserAccount(it.sessionId!!) }
                .doOnNext { saveUserProfile(it) }
    }

    private fun saveUserProfile(account: Account?) {
        account?.apply {
            with(preferenceHelper) {
                setId(id ?: 0L)
                setName(name)
                setUserName(username)
                setGravatarHash(avatar?.gravatar?.hash)
            }
        }
    }

    fun markAsFavorite(favorite: Favorite): Completable {
        return authApi.markAsFavorite(preferenceHelper.getId(), favorite)
    }
}