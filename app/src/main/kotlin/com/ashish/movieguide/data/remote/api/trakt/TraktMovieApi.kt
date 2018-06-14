package com.ashish.movieguide.data.remote.api.trakt

import com.ashish.movieguide.data.remote.entities.trakt.TraktMovie
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TraktMovieApi {

    @GET("movies/{id}")
    fun getMovieDetail(
            @Path("id") id: String,
            @Query("extended") extended: String? = null
    ): Observable<TraktMovie>
}