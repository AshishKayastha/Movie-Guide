package com.ashish.movieguide.data.interactors

import com.ashish.movieguide.data.network.api.tmdb.AuthApi
import com.ashish.movieguide.data.network.entities.tmdb.Episode
import com.ashish.movieguide.data.network.entities.tmdb.Favorite
import com.ashish.movieguide.data.network.entities.tmdb.Movie
import com.ashish.movieguide.data.network.entities.tmdb.Results
import com.ashish.movieguide.data.network.entities.tmdb.Status
import com.ashish.movieguide.data.network.entities.tmdb.TVShow
import com.ashish.movieguide.data.network.entities.tmdb.Watchlist
import com.ashish.movieguide.data.preferences.PreferenceHelper
import com.ashish.movieguide.utils.AuthException
import io.reactivex.Single
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

    fun markAsFavorite(favorite: Favorite): Single<Status> {
        return callApiIfLoggedIn { accountId -> authApi.markAsFavorite(accountId, favorite) }
    }

    fun addToWatchlist(watchlist: Watchlist): Single<Status> {
        return callApiIfLoggedIn { accountId -> authApi.addToWatchlist(accountId, watchlist) }
    }

    fun getPersonalMoviesByType(type: String, page: Int): Single<Results<Movie>> {
        return callApiIfLoggedIn { accountId ->
            authApi.getPersonalMoviesByType(type, accountId, page)
        }
    }

    fun getPersonalTVShowsByType(type: String, page: Int): Single<Results<TVShow>> {
        return callApiIfLoggedIn { accountId ->
            authApi.getPersonalTVShowsByType(type, accountId, page)
        }
    }

    fun getRatedMovies(page: Int): Single<Results<Movie>> {
        return callApiIfLoggedIn { accountId -> authApi.getRatedMovies(accountId, page) }
    }

    fun getRatedTVShows(page: Int): Single<Results<TVShow>> {
        return callApiIfLoggedIn { accountId -> authApi.getRatedTVShows(accountId, page) }
    }

    fun getRatedEpisodes(page: Int): Single<Results<Episode>> {
        return callApiIfLoggedIn { accountId -> authApi.getRatedEpisodes(accountId, page) }
    }

    private fun <T> callApiIfLoggedIn(actionIfLoggedIn: (accountId: Long) -> Single<T>): Single<T> {
        val accountId = preferenceHelper.getId()
        return if (accountId > 0) {
            actionIfLoggedIn(accountId)
        } else {
            Single.error(AuthException())
        }
    }
}