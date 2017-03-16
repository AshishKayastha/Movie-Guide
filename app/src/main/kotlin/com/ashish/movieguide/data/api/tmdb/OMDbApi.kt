package com.ashish.movieguide.data.api.tmdb

import com.ashish.movieguide.data.models.common.OMDbDetail
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Ashish on Jan 09.
 */
interface OMDbApi {

    @GET("?plot=full&tomatoes=true&r=json")
    fun getDetailFromIMDbId(@Query("i") imdbId: String): Single<OMDbDetail>
}