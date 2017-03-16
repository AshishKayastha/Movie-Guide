package com.ashish.movieguide.data.api.trakt

import com.ashish.movieguide.data.models.trakt.TraktMovie
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TraktMovieApi {

    @GET("movies/{id}")
    fun getMovieDetail(
            @Path("id") id: String,
            @Query("extended") extended: String? = null
    ): Single<TraktMovie>
}