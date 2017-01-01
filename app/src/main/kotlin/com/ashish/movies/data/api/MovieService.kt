package com.ashish.movies.data.api

import com.ashish.movies.data.models.Movie
import com.ashish.movies.data.models.MovieDetail
import com.ashish.movies.data.models.Results
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable

/**
 * Created by Ashish on Dec 27.
 */
interface MovieService {

    companion object {
        const val NOW_PLAYING = "now_playing"
        const val POPULAR = "popular"
        const val TOP_RATED = "top_rated"
        const val UPCOMING = "upcoming"
    }

    @GET("movie/{movieType}")
    fun getMovies(@Path("movieType") movieType: String?, @Query("page") page: Int = 1): Observable<Results<Movie>>

    @GET("movie/{movieId}")
    fun getMovieDetail(@Path("movieId") movieId: Long): Observable<MovieDetail>
}