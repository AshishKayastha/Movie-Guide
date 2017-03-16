package com.ashish.movieguide.data.api.trakt

import com.ashish.movieguide.data.models.trakt.TraktEpisode
import com.ashish.movieguide.data.models.trakt.TraktSeason
import com.ashish.movieguide.data.models.trakt.TraktShow
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TraktShowApi {

    @GET("shows/{showId}")
    fun getShowDetail(
            @Path("showId") id: String,
            @Query("extended") extended: String? = null
    ): Single<TraktShow>

    @GET("shows/{showId}/season/{seasonNumber}")
    fun getSeasonDetail(
            @Path("showId") id: String,
            @Path("seasonNumber") seasonNumber: Int,
            @Query("extended") extended: String? = null
    ): Single<TraktSeason>

    @GET("shows/{showId}/season/{seasonNumber}/episodes/{episodeNumber}")
    fun getEpisodeDetail(
            @Path("showId") id: String,
            @Path("seasonNumber") seasonNumber: Int,
            @Path("episodeNumber") episodeNumber: Int,
            @Query("extended") extended: String? = null
    ): Single<TraktEpisode>
}