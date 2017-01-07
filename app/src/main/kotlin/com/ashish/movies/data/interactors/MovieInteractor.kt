package com.ashish.movies.data.interactors

import com.ashish.movies.data.api.MovieApi
import com.ashish.movies.data.models.Movie
import com.ashish.movies.data.models.MovieDetail
import com.ashish.movies.data.models.Results
import com.ashish.movies.utils.ApiConstants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ashish on Dec 28.
 */
@Singleton
class MovieInteractor @Inject constructor(val movieApi: MovieApi) {

    fun getMoviesByType(movieType: String?, page: Int = 1): Observable<Results<Movie>> {
        return movieApi.getMovies(movieType, page).observeOn(AndroidSchedulers.mainThread())
    }

    fun getMovieDetailWithCreditsAndSimilarMovies(movieId: Long): Observable<MovieDetail> {
        return movieApi.getMovieDetailWithAppendedResponse(movieId, ApiConstants.CREDITS_AND_SIMILAR)
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun discoverMovie(sortBy: String, year: Int, genres: String? = null, page: Int): Observable<Results<Movie>> {
        return movieApi.discoverMovie(sortBy, year, genres, page)
                .observeOn(AndroidSchedulers.mainThread())
    }
}