package com.ashish.movieguide.data.interactors

import com.ashish.movieguide.data.api.AuthApi
import com.ashish.movieguide.data.models.Account
import com.ashish.movieguide.data.models.Episode
import com.ashish.movieguide.data.models.Favorite
import com.ashish.movieguide.data.models.Movie
import com.ashish.movieguide.data.models.RequestToken
import com.ashish.movieguide.data.models.Results
import com.ashish.movieguide.data.models.Status
import com.ashish.movieguide.data.models.TVShow
import com.ashish.movieguide.data.models.Watchlist
import com.ashish.movieguide.data.preferences.PreferenceHelper
import com.ashish.movieguide.utils.AuthException
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

    fun markAsFavorite(favorite: Favorite): Observable<Status> {
        return callApiIfLoggedIn { accountId ->
            authApi.markAsFavorite(accountId, favorite)
        }
    }

    fun addToWatchlist(watchlist: Watchlist): Observable<Status> {
        return callApiIfLoggedIn { accountId ->
            authApi.addToWatchlist(accountId, watchlist)
        }
    }

    fun getPersonalMoviesByType(type: String, page: Int): Observable<Results<Movie>> {
        return callApiIfLoggedIn { accountId ->
            authApi.getPersonalMoviesByType(type, accountId, page)
        }
    }

    fun getPersonalTVShowsByType(type: String, page: Int): Observable<Results<TVShow>> {
        return callApiIfLoggedIn { accountId ->
            authApi.getPersonalTVShowsByType(type, accountId, page)
        }
    }

    fun getRatedMovies(page: Int): Observable<Results<Movie>> {
        return callApiIfLoggedIn { accountId -> authApi.getRatedMovies(accountId, page) }
    }

    fun getRatedTVShows(page: Int): Observable<Results<TVShow>> {
        return callApiIfLoggedIn { accountId -> authApi.getRatedTVShows(accountId, page) }
    }

    fun getRatedEpisodes(page: Int): Observable<Results<Episode>> {
        return callApiIfLoggedIn { accountId -> authApi.getRatedEpisodes(accountId, page) }
    }

    private fun <T> callApiIfLoggedIn(actionIfLoggedIn: (accountId: Long) -> Observable<T>): Observable<T> {
        val accountId = preferenceHelper.getId()
        if (accountId > 0) {
            return actionIfLoggedIn(accountId)
        } else {
            return Observable.error(AuthException())
        }
    }
}