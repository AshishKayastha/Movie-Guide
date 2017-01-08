package com.ashish.movies.data.interactors

import com.ashish.movies.data.api.MovieApi
import com.ashish.movies.data.models.Movie
import com.ashish.movies.data.models.MovieDetail
import com.ashish.movies.data.models.Results
import com.ashish.movies.utils.ApiConstants.CREDITS_AND_SIMILAR
import com.ashish.movies.utils.extensions.observeOnMainThread
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ashish on Dec 28.
 */
@Singleton
class MovieInteractor @Inject constructor(val movieApi: MovieApi) {

    fun getMoviesByType(movieType: String?, page: Int = 1): Observable<Results<Movie>> {
        return movieApi.getMovies(movieType, page).observeOnMainThread()
    }

    fun getMovieDetailWithCreditsAndSimilarMovies(movieId: Long): Observable<MovieDetail> {
        return movieApi.getMovieDetailWithAppendedResponse(movieId, CREDITS_AND_SIMILAR)
                .observeOnMainThread()
    }

    fun discoverMovie(sortBy: String, year: Int, genres: String? = null, page: Int): Observable<Results<Movie>> {
        return movieApi.discoverMovie(sortBy, year, genres, page).observeOnMainThread()
    }
}