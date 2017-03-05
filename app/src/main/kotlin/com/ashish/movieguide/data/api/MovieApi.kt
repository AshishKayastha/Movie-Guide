package com.ashish.movieguide.data.api

import com.ashish.movieguide.data.models.Movie
import com.ashish.movieguide.data.models.MovieDetail
import com.ashish.movieguide.data.models.Rating
import com.ashish.movieguide.data.models.Results
import com.ashish.movieguide.data.models.Status
import com.ashish.movieguide.utils.Constants.INCLUDE_IMAGE_LANGUAGE
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Ashish on Dec 27.
 */
interface MovieApi {

    companion object {
        const val NOW_PLAYING = "now_playing"
        const val POPULAR = "popular"
        const val TOP_RATED = "top_rated"
        const val UPCOMING = "upcoming"
    }

    @GET("movie/{movieType}")
    fun getMovies(
            @Path("movieType") movieType: String?,
            @Query("page") page: Int = 1
    ): Observable<Results<Movie>>

    @GET("movie/{movieId}" + INCLUDE_IMAGE_LANGUAGE)
    fun getMovieDetail(
            @Path("movieId") movieId: Long,
            @Query("append_to_response") appendedResponse: String
    ): Observable<MovieDetail>

    @POST("movie/{movieId}/rating")
    fun rateMovie(@Path("movieId") movieId: Long, @Body rating: Rating): Single<Status>

    @GET("discover/movie")
    fun discoverMovie(
            @Query("sort_by") sortBy: String = "popularity.desc",
            @Query("release_date.gte") minReleaseDate: String? = null,
            @Query("release_date.lte") maxReleaseDate: String? = null,
            @Query("with_genres") genreIds: String? = null,
            @Query("page") page: Int = 1
    ): Observable<Results<Movie>>
}