package com.ashish.movies.data.interactors

import com.ashish.movies.data.api.MovieService
import com.ashish.movies.data.models.MovieResults
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
 * Created by Ashish on Dec 28.
 */
class MovieInteractor @Inject constructor(val movieService: MovieService) {

    fun getMoviesByType(movieType: String, page: Int = 1): Observable<MovieResults> {
        return movieService.getMovies(movieType, page)
                .observeOn(AndroidSchedulers.mainThread())
    }
}