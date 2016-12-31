package com.ashish.movies.data.api

import com.ashish.movies.data.models.People
import com.ashish.movies.data.models.Results
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

/**
 * Created by Ashish on Dec 31.
 */
interface PeopleService {

    @GET("person/popular")
    fun getPopularPeople(@Query("page") page: Int = 1): Observable<Results<People>>
}