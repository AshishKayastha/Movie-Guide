package com.ashish.movieguide.data.network.api.tmdb

import com.ashish.movieguide.data.network.entities.common.OMDbDetail
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Ashish on Jan 09.
 */
interface OMDbApi {

    @GET("?plot=full&tomatoes=true&r=json")
    fun getOMDbDetail(@Query("i") imdbId: String): Observable<OMDbDetail>
}