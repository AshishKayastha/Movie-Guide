package com.ashish.movies.data.api

import com.ashish.movies.data.models.Results
import com.ashish.movies.data.models.TVShow
import com.ashish.movies.data.models.TVShowDetail
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Ashish on Dec 29.
 */
interface TVShowApi {

    companion object {
        const val ON_THE_AIR = "on_the_air"
        const val POPULAR = "popular"
        const val TOP_RATED = "top_rated"
        const val AIRING_TODAY = "airing_today"
    }

    @GET("tv/{tvShowType}")
    fun getTVShows(@Path("tvShowType") tvShowType: String?, @Query("page") page: Int = 1): Observable<Results<TVShow>>

    @GET("tv/{tvId}")
    fun getTVShowDetailWithAppendedResponse(@Path("tvId") tvId: Long,
                                            @Query("append_to_response") appendedResponse: String): Observable<TVShowDetail>

    @GET("discover/tv")
    fun discoverTVShow(@Query("sort_by") sortBy: String = "popularity.desc",
                       @Query("first_air_date_year") year: Int = 2016,
                       @Query("with_genres") genres: String? = null,
                       @Query("page") page: Int = 1): Observable<Results<TVShow>>
}