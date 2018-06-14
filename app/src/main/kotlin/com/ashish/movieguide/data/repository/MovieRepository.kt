package com.ashish.movieguide.data.repository

import com.ashish.movieguide.data.local.datasource.MovieLocalDataSource
import com.ashish.movieguide.data.local.entities.LocalMovie
import com.ashish.movieguide.data.remote.interactors.MovieInteractor
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
        private val movieInteractor: MovieInteractor,
        private val movieLocalDataSource: MovieLocalDataSource
) {

    fun getMovieDetail(movieId: Long): Observable<LocalMovie> {
        return movieInteractor.getMovieDetail(movieId)
                .flatMap { movieLocalDataSource.putMovieDetail(it) }
                .startWith { movieLocalDataSource.getMovie(movieId) }
    }
}