package com.ashish.movieguide.data.local.datasource

import com.ashish.movieguide.data.local.entities.LocalMovie
import com.ashish.movieguide.data.local.entities.LocalShow
import com.ashish.movieguide.data.local.entities.MovieEntity
import com.ashish.movieguide.data.local.entities.ShowEntity
import com.ashish.movieguide.data.remote.entities.tmdb.Favorite
import com.ashish.movieguide.data.remote.entities.tmdb.Watchlist
import com.ashish.movieguide.utils.TMDbConstants
import io.reactivex.Completable
import io.requery.Persistable
import io.requery.reactivex.KotlinReactiveEntityStore
import javax.inject.Inject

class AuthLocalDataSource @Inject constructor(
        private val dataStore: KotlinReactiveEntityStore<Persistable>
) {

    fun markAsFavorite(favorite: Favorite): Completable {
        return when (favorite.mediaType) {
            TMDbConstants.MEDIA_TYPE_MOVIE -> {
                val movie = getMovie(favorite.mediaId)
                movie.isFavorite = favorite.favorite ?: false
                dataStore.update(movie).ignoreElement()
            }

            TMDbConstants.MEDIA_TYPE_TV -> {
                val show = getTVShow(favorite.mediaId)
                show.isFavorite = favorite.favorite ?: false
                dataStore.update(show).ignoreElement()
            }

            else -> showError(favorite.mediaId!!)
        }
    }

    fun addToWatchlist(watchlist: Watchlist): Completable {
        return when (watchlist.mediaType) {
            TMDbConstants.MEDIA_TYPE_MOVIE -> {
                val movie = getMovie(watchlist.mediaId)
                movie.inWatchlist = watchlist.watchlist ?: false
                dataStore.update(movie).ignoreElement()
            }

            TMDbConstants.MEDIA_TYPE_TV -> {
                val show = getTVShow(watchlist.mediaId)
                show.isFavorite = watchlist.watchlist ?: false
                dataStore.update(show).ignoreElement()
            }

            else -> showError(watchlist.mediaId!!)
        }
    }

    private fun getMovie(movieId: Long?): LocalMovie {
        return dataStore.select(LocalMovie::class)
                .where(MovieEntity.TMDB_ID.eq(movieId))
                .get()
                .first()
    }

    private fun getTVShow(showId: Long?): LocalShow {
        return dataStore.select(LocalShow::class)
                .where(ShowEntity.TMDB_ID.eq(showId))
                .get()
                .first()
    }

    private fun showError(mediaId: Long): Completable {
        return Completable.error(Throwable("No media found with media id $mediaId"))
    }
}