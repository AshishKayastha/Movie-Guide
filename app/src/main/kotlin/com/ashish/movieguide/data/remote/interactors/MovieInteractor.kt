package com.ashish.movieguide.data.remote.interactors

import com.ashish.movieguide.data.remote.api.tmdb.MovieApi
import com.ashish.movieguide.data.remote.api.tmdb.OMDbApi
import com.ashish.movieguide.data.remote.api.trakt.TraktMovieApi
import com.ashish.movieguide.data.remote.entities.common.FullDetailContent
import com.ashish.movieguide.data.remote.entities.common.OMDbDetail
import com.ashish.movieguide.data.remote.entities.tmdb.Movie
import com.ashish.movieguide.data.remote.entities.tmdb.MovieDetail
import com.ashish.movieguide.data.remote.entities.tmdb.Results
import com.ashish.movieguide.data.remote.entities.tmdb.Review
import com.ashish.movieguide.data.remote.entities.trakt.TraktMovie
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ashish on Dec 28.
 */
@Singleton
class MovieInteractor @Inject constructor(
        private val movieApi: MovieApi,
        private val omDbApi: OMDbApi,
        private val traktMovieApi: TraktMovieApi
) {

    fun getMoviesByType(movieType: String?, page: Int = 1): Single<Results<Movie>> {
        return movieApi.getMovies(movieType, page)
    }

    fun getMovieDetail(movieId: Long): Observable<FullDetailContent<MovieDetail, TraktMovie>> {
        return movieApi.getMovieDetail(movieId, "credits,similar,images,videos")
                .flatMap { convertToFullMovieDetail(it) }
    }

    private fun convertToFullMovieDetail(
            movieDetail: MovieDetail
    ): Observable<FullDetailContent<MovieDetail, TraktMovie>> {
        val imdbId = movieDetail.imdbId
        return if (imdbId.isNotNullOrEmpty()) {
            val traktMovieObservable = traktMovieApi.getMovieDetail(imdbId!!)
                    .onErrorReturnItem(TraktMovie())

            val omdbDetailObservable = omDbApi.getOMDbDetail(imdbId)
                    .onErrorReturnItem(OMDbDetail())

            Observable.zip(traktMovieObservable, omdbDetailObservable, BiFunction { traktMovie, omDbDetail ->
                FullDetailContent(movieDetail, omDbDetail, traktMovie)
            })
        } else {
            Observable.just(FullDetailContent(movieDetail))
        }
    }

    fun getMovieReviews(movieId: Long, page: Int): Single<Results<Review>> {
        return movieApi.getMovieReviews(movieId, page)
    }

    fun discoverMovie(
            sortBy: String,
            minReleaseDate: String?,
            maxReleaseDate: String?,
            genreIds: String?,
            page: Int
    ): Single<Results<Movie>> {
        return movieApi.discoverMovie(sortBy, minReleaseDate, maxReleaseDate, genreIds, page)
    }
}