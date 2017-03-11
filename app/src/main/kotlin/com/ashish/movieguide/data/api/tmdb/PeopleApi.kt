package com.ashish.movieguide.data.api.tmdb

import com.ashish.movieguide.data.models.Person
import com.ashish.movieguide.data.models.PersonDetail
import com.ashish.movieguide.data.models.Results
import com.ashish.movieguide.utils.Constants.INCLUDE_IMAGE_LANGUAGE
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Ashish on Dec 31.
 */
interface PeopleApi {

    @GET("person/popular")
    fun getPopularPeople(@Query("page") page: Int = 1): Single<Results<Person>>

    @GET("person/{personId}" + INCLUDE_IMAGE_LANGUAGE)
    fun getPersonDetail(
            @Path("personId") personId: Long,
            @Query("append_to_response") appendedResponse: String
    ): Single<PersonDetail>
}