package com.ashish.movieguide.data.interactors

import com.ashish.movieguide.data.api.MovieApi
import com.ashish.movieguide.data.api.OMDbApi
import com.ashish.movieguide.data.models.FullDetailContent
import com.ashish.movieguide.data.models.Movie
import com.ashish.movieguide.data.models.MovieDetail
import com.ashish.movieguide.data.models.Rating
import com.ashish.movieguide.data.models.Results
import com.ashish.movieguide.data.models.Review
import com.ashish.movieguide.data.models.Status
import com.ashish.movieguide.utils.extensions.convertToFullDetailContent
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ashish on Dec 28.
 */
@Singleton
class MovieInteractor @Inject constructor(
        private val movieApi: MovieApi,
        private val omDbApi: OMDbApi
) {

    fun getMoviesByType(movieType: String?, page: Int = 1): Single<Results<Movie>> {
        return movieApi.getMovies(movieType, page)
    }

    fun getFullMovieDetail(movieId: Long): Single<FullDetailContent<MovieDetail>> {
        return movieApi.getMovieDetail(movieId, "credits,similar,images,videos,account_states")
                .flatMap { omDbApi.convertToFullDetailContent(it.imdbId, it) }
    }

    fun rateMovie(movieId: Long, value: Double): Single<Status> {
        return movieApi.rateMovie(movieId, Rating(value))
    }

    fun deleteMovieRating(movieId: Long): Single<Status> {
        return movieApi.deleteMovieRating(movieId)
    }

    fun getMovieReviews(movieId: Long, page: Int): Single<Results<Review>> {
        return movieApi.getMovieReviews(movieId, page)
    }

    fun discoverMovie(sortBy: String, minReleaseDate: String?, maxReleaseDate: String?, genreIds: String?,
                      page: Int): Single<Results<Movie>> {
        return movieApi.discoverMovie(sortBy, minReleaseDate, maxReleaseDate, genreIds, page)
    }
}