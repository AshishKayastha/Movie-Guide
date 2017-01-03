package com.ashish.movies.data.interactors

import com.ashish.movies.data.api.MovieApi
import com.ashish.movies.data.models.CreditResults
import com.ashish.movies.data.models.Movie
import com.ashish.movies.data.models.Results
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

    fun getMovieDetail(movieId: Long): Observable<Movie> {
        return movieApi.getMovieDetail(movieId).observeOn(AndroidSchedulers.mainThread())
    }

    fun getMovieCredits(movieId: Long): Observable<CreditResults> {
        return movieApi.getMovieCredits(movieId).observeOn(AndroidSchedulers.mainThread())
    }

    fun getSimilarMovies(movieId: Long): Observable<Results<Movie>> {
        return movieApi.getSimilarMovies(movieId).observeOn(AndroidSchedulers.mainThread())
    }
}