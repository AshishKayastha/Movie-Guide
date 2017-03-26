package com.ashish.movieguide.data.network.api.trakt

import com.ashish.movieguide.data.network.entities.trakt.SyncItems
import io.reactivex.Completable
import retrofit2.http.Body
import retrofit2.http.POST

interface SyncApi {

    @POST("sync/ratings")
    fun addRatings(@Body syncItems: SyncItems): Completable

    @POST("sync/ratings/remove")
    fun removeRatings(@Body syncItems: SyncItems): Completable
}