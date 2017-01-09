package com.ashish.movies.data.api

import com.ashish.movies.data.models.OMDbDetail
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Ashish on Jan 09.
 */
interface OMDbApi {

    @GET("?plot=full&tomatoes=true&r=json")
    fun getDetailFromIMDbId(@Query("i") imdbId: String): Observable<OMDbDetail>
}