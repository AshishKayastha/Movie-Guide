package com.ashish.movies.data.api

import com.ashish.movies.data.models.CreditResults
import com.ashish.movies.data.models.Results
import com.ashish.movies.data.models.TVShow
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
    fun getTVShowDetail(@Path("tvId") tvId: Long): Observable<TVShow>

    @GET("tv/{tvId}/credits")
    fun getTVShowCredits(@Path("tvId") tvId: Long): Observable<CreditResults>

    @GET("tv/{tvId}/similar")
    fun getSimilarTVShows(@Path("tvId") tvId: Long): Observable<Results<TVShow>>
}