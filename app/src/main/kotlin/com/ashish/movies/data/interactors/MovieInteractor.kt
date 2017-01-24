package com.ashish.movies.data.interactors

import com.ashish.movies.data.api.MovieApi
import com.ashish.movies.data.api.OMDbApi
import com.ashish.movies.data.models.FullDetailContent
import com.ashish.movies.data.models.Movie
import com.ashish.movies.data.models.MovieDetail
import com.ashish.movies.data.models.Results
import com.ashish.movies.utils.extensions.convertToFullDetailContent
import com.ashish.movies.utils.extensions.observeOnMainThread
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ashish on Dec 28.
 */
@Singleton
class MovieInteractor @Inject constructor(private val movieApi: MovieApi, private val omDbApi: OMDbApi) {

    fun getMoviesByType(movieType: String?, page: Int = 1): Observable<Results<Movie>> {
        return movieApi.getMovies(movieType, page).observeOnMainThread()
    }

    fun getFullMovieDetail(movieId: Long): Observable<FullDetailContent<MovieDetail>> {
        return movieApi.getMovieDetail(movieId, "credits,similar,images,videos")
                .flatMap { omDbApi.convertToFullDetailContent(it.imdbId, it) }
                .observeOnMainThread()
    }

    fun discoverMovie(sortBy: String, minReleaseDate: String?, maxReleaseDate: String?, genreIds: String?,
                      page: Int): Observable<Results<Movie>> {
        return movieApi.discoverMovie(sortBy, minReleaseDate, maxReleaseDate, genreIds, page)
                .observeOnMainThread()
    }
}