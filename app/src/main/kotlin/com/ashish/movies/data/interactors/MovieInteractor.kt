package com.ashish.movies.data.interactors

import com.ashish.movies.data.api.MovieService
import com.ashish.movies.data.models.MovieResults
import rx.Observable
import javax.inject.Inject

/**
 * Created by Ashish on Dec 28.
 */
class MovieInteractor @Inject constructor(val movieService: MovieService) {

    fun getMovies(movieType: String, page: Int? = null): Observable<MovieResults> {
        return movieService.getMovies(movieType, page)
    }
}