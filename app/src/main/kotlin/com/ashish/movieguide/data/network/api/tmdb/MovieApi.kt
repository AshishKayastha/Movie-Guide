package com.ashish.movieguide.data.network.api.tmdb

import com.ashish.movieguide.data.network.entities.tmdb.Movie
import com.ashish.movieguide.data.network.entities.tmdb.MovieDetail
import com.ashish.movieguide.data.network.entities.tmdb.Results
import com.ashish.movieguide.data.network.entities.tmdb.Review
import com.ashish.movieguide.utils.TMDbConstants.INCLUDE_IMAGE_LANGUAGE
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
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
            @Query("page") page: Int = 1,
            @Query("region") region: String = "US"
    ): Single<Results<Movie>>

    @GET("movie/{movieId}" + INCLUDE_IMAGE_LANGUAGE)
    fun getMovieDetail(
            @Path("movieId") movieId: Long,
            @Query("append_to_response") appendedResponse: String
    ): Observable<MovieDetail>

    @GET("movie/{movieId}/reviews")
    fun getMovieReviews(
            @Path("movieId") movieId: Long,
            @Query("page") page: Int = 1
    ): Single<Results<Review>>

    @GET("discover/movie")
    fun discoverMovie(
            @Query("sort_by") sortBy: String = "popularity.desc",
            @Query("release_date.gte") minReleaseDate: String? = null,
            @Query("release_date.lte") maxReleaseDate: String? = null,
            @Query("with_genres") genreIds: String? = null,
            @Query("page") page: Int = 1,
            @Query("region") region: String = "US",
            @Query("with_release_type") releaseType: String = "2|3"
    ): Single<Results<Movie>>
}