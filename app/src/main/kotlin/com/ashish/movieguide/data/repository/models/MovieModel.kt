package com.ashish.movieguide.data.repository.models

import com.ashish.movieguide.data.database.entities.Movie
import com.ashish.movieguide.data.interactors.MovieInteractor
import com.ashish.movieguide.data.repository.local.LocalMovieRepository
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieModel @Inject constructor(
        private val movieInteractor: MovieInteractor,
        private val localMovieRepository: LocalMovieRepository
) {

    fun getMovieDetail(movieId: Long): Observable<Movie> {
        return movieInteractor.getMovieDetail(movieId)
                .flatMap { localMovieRepository.putMovieDetail(it) }
                .startWith { localMovieRepository.getMovie(movieId) }
    }
}