package com.ashish.movieguide.data.interactors

import com.ashish.movieguide.data.api.tmdb.MovieApi
import com.ashish.movieguide.data.api.tmdb.OMDbApi
import com.ashish.movieguide.data.api.trakt.TraktMovieApi
import com.ashish.movieguide.data.models.common.FullDetailContent
import com.ashish.movieguide.data.models.common.OMDbDetail
import com.ashish.movieguide.data.models.tmdb.Movie
import com.ashish.movieguide.data.models.tmdb.MovieDetail
import com.ashish.movieguide.data.models.tmdb.Results
import com.ashish.movieguide.data.models.tmdb.Review
import com.ashish.movieguide.data.models.trakt.TraktMovie
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
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

    fun getFullMovieDetail(movieId: Long): Single<FullDetailContent<MovieDetail, TraktMovie>> {
        return movieApi.getMovieDetail(movieId, "credits,similar,images,videos")
                .flatMap { convertToFullMovieDetail(it) }
    }

    private fun convertToFullMovieDetail(movieDetail: MovieDetail): Single<FullDetailContent<MovieDetail, TraktMovie>> {
        val imdbId = movieDetail.imdbId
        if (imdbId.isNotNullOrEmpty()) {
            val traktMovieSingle = traktMovieApi.getMovieDetail(imdbId!!)
                    .onErrorReturnItem(TraktMovie())

            val omdbDetailSingle = omDbApi.getDetailFromIMDbId(imdbId)
                    .onErrorReturnItem(OMDbDetail())

            return Single.zip(traktMovieSingle, omdbDetailSingle, BiFunction { traktMovie, omDbDetail ->
                FullDetailContent(movieDetail, omDbDetail, traktMovie)
            })
        } else {
            return Single.just(FullDetailContent(movieDetail))
        }
    }

    fun getMovieReviews(movieId: Long, page: Int): Single<Results<Review>> {
        return movieApi.getMovieReviews(movieId, page)
    }

    fun discoverMovie(sortBy: String, minReleaseDate: String?, maxReleaseDate: String?, genreIds: String?,
                      page: Int): Single<Results<Movie>> {
        return movieApi.discoverMovie(sortBy, minReleaseDate, maxReleaseDate, genreIds, page)
    }
}