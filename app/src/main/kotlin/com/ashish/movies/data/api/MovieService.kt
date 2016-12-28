package com.ashish.movies.data.api

import com.ashish.movies.data.models.MovieResults
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

/**
 * Created by Ashish on Dec 27.
 */
interface MovieService {

    @GET("movie/now_playing")
    fun getNowPlayingMovies(@Query("page") page: Int? = null): Observable<MovieResults>

    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int? = null): Observable<MovieResults>

    @GET("movie/upcoming")
    fun getUpcomingMovies(@Query("page") page: Int? = null): Observable<MovieResults>

    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("page") page: Int? = null): Observable<MovieResults>
}