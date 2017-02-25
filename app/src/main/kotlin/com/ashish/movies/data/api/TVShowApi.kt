package com.ashish.movies.data.api

import com.ashish.movies.data.models.EpisodeDetail
import com.ashish.movies.data.models.Results
import com.ashish.movies.data.models.SeasonDetail
import com.ashish.movies.data.models.TVShow
import com.ashish.movies.data.models.TVShowDetail
import com.ashish.movies.utils.Constants.INCLUDE_IMAGE_LANGUAGE
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Ashish on Dec 29.
 */
interface TVShowApi {

    companion object {
        const val ON_THE_AIR = "on_the_air"
        const val POPULAR = "popular"
        const val TOP_RATED = "top_rated"
        const val AIRING_TODAY = "airing_today"
    }

    @GET("tv/{tvShowType}")
    fun getTVShows(
            @Path("tvShowType") tvShowType: String?,
            @Query("page") page: Int = 1
    ): Observable<Results<TVShow>>

    @GET("tv/{tvId}" + INCLUDE_IMAGE_LANGUAGE)
    fun getTVShowDetail(
            @Path("tvId") tvId: Long,
            @Query("append_to_response") appendedResponse: String
    ): Observable<TVShowDetail>

    @GET("tv/{tvId}/season/{seasonNumber}" + INCLUDE_IMAGE_LANGUAGE)
    fun getSeasonDetail(
            @Path("tvId") tvId: Long,
            @Path("seasonNumber") seasonNumber: Int,
            @Query("append_to_response") appendedResponse: String
    ): Observable<SeasonDetail>

    @GET("tv/{tvId}/season/{seasonNumber}/episode/{episodeNumber}" + INCLUDE_IMAGE_LANGUAGE)
    fun getEpisodeDetail(
            @Path("tvId") tvId: Long,
            @Path("seasonNumber") seasonNumber: Int,
            @Path("episodeNumber") episodeNumber: Int,
            @Query("append_to_response") appendedResponse: String
    ): Observable<EpisodeDetail>

    @GET("discover/tv")
    fun discoverTVShow(
            @Query("sort_by") sortBy: String = "popularity.desc",
            @Query("air_date.gte") minAirDate: String? = null,
            @Query("air_date.lte") maxAirDate: String? = null,
            @Query("with_genres") genreIds: String? = null,
            @Query("page") page: Int = 1
    ): Observable<Results<TVShow>>
}