package com.ashish.movies.data.interactors

import com.ashish.movies.data.api.MovieService
import com.ashish.movies.data.models.Movie
import com.ashish.movies.data.models.Results
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ashish on Dec 28.
 */
@Singleton
class MovieInteractor @Inject constructor(val movieService: MovieService) {

    fun getMoviesByType(movieType: String, page: Int = 1): Observable<Results<Movie>> {
        return movieService.getMovies(movieType, page)
                .observeOn(AndroidSchedulers.mainThread())
    }
}