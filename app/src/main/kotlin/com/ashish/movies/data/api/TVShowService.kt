package com.ashish.movies.data.api

import com.ashish.movies.data.models.Results
import com.ashish.movies.data.models.TVShow
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable

/**
 * Created by Ashish on Dec 29.
 */
interface TVShowService {

    companion object {
        const val ON_THE_AIR = "on_the_air"
        const val POPULAR = "popular"
        const val TOP_RATED = "top_rated"
        const val AIRING_TODAY = "airing_today"
    }

    @GET("tv/{tvShowType}")
    fun getTVShows(@Path("tvShowType") tvShowType: String?, @Query("page") page: Int = 1): Observable<Results<TVShow>>
}