package com.ashish.movieguide.data.network.api.trakt

import com.ashish.movieguide.data.network.entities.trakt.Settings
import com.ashish.movieguide.data.network.entities.trakt.Stats
import com.ashish.movieguide.data.network.entities.trakt.UserProfile
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {

    @GET("users/{username}")
    fun getProfile(
            @Path("username") userSlug: String,
            @Query("extended") extended: String = "full"
    ): Single<UserProfile>

    @GET("users/settings")
    fun getUserSettings(): Single<Settings>

    @GET("users/{slug}/stats")
    fun getUserStats(@Path("slug") slug: String): Single<Stats>
}