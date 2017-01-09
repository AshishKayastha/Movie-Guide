package com.ashish.movies.data.api

import com.ashish.movies.data.models.Person
import com.ashish.movies.data.models.PersonDetail
import com.ashish.movies.data.models.Results
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Ashish on Dec 31.
 */
interface PeopleApi {

    @GET("person/popular")
    fun getPopularPeople(@Query("page") page: Int = 1): Observable<Results<Person>>

    @GET("person/{personId}")
    fun getPeopleDetail(@Path("personId") personId: Long,
                        @Query("append_to_response") appendedResponse: String): Observable<PersonDetail>
}