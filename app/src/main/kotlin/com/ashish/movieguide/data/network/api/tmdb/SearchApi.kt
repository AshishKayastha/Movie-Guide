package com.ashish.movieguide.data.network.api.tmdb

import com.ashish.movieguide.data.network.entities.tmdb.MultiSearch
import com.ashish.movieguide.data.network.entities.tmdb.Results
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Ashish on Jan 05.
 */
interface SearchApi {

    @GET("search/multi")
    fun getMultiSearchResults(
            @Query("query") query: String,
            @Query("page") page: Int = 1
    ): Single<Results<MultiSearch>>
}