package com.ashish.movies.data.api

import com.ashish.movies.data.models.MultiSearch
import com.ashish.movies.data.models.Results
import io.reactivex.Observable
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
    ): Observable<Results<MultiSearch>>
}