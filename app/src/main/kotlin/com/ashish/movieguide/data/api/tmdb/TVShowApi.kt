package com.ashish.movieguide.data.api.tmdb

import com.ashish.movieguide.data.models.EpisodeDetail
import com.ashish.movieguide.data.models.Rating
import com.ashish.movieguide.data.models.Results
import com.ashish.movieguide.data.models.SeasonDetail
import com.ashish.movieguide.data.models.Status
import com.ashish.movieguide.data.models.TVShow
import com.ashish.movieguide.data.models.TVShowDetail
import com.ashish.movieguide.utils.Constants.INCLUDE_IMAGE_LANGUAGE
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
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

    /* TV Shows */

    @GET("tv/{tvShowType}")
    fun getTVShows(
            @Path("tvShowType") tvShowType: String?,
            @Query("page") page: Int = 1
    ): Single<Results<TVShow>>

    @GET("tv/{tvId}" + INCLUDE_IMAGE_LANGUAGE)
    fun getTVShowDetail(
            @Path("tvId") tvId: Long,
            @Query("append_to_response") appendedResponse: String
    ): Single<TVShowDetail>

    @POST("tv/{tvId}/rating")
    fun rateTVShow(@Path("tvId") tvId: Long, @Body rating: Rating): Single<Status>

    @DELETE("tv/{tvId}/rating")
    fun deleteTVRating(@Path("tvId") tvId: Long): Single<Status>


    /* Season */

    @GET("tv/{tvId}/season/{seasonNumber}" + INCLUDE_IMAGE_LANGUAGE)
    fun getSeasonDetail(
            @Path("tvId") tvId: Long,
            @Path("seasonNumber") seasonNumber: Int,
            @Query("append_to_response") appendedResponse: String
    ): Single<SeasonDetail>


    /* Episode */

    @GET("tv/{tvId}/season/{seasonNumber}/episode/{episodeNumber}" + INCLUDE_IMAGE_LANGUAGE)
    fun getEpisodeDetail(
            @Path("tvId") tvId: Long,
            @Path("seasonNumber") seasonNumber: Int,
            @Path("episodeNumber") episodeNumber: Int,
            @Query("append_to_response") appendedResponse: String
    ): Single<EpisodeDetail>

    @POST("tv/{tvId}/season/{seasonNumber}/episode/{episodeNumber}/rating")
    fun rateEpisode(
            @Path("tvId") tvId: Long,
            @Path("seasonNumber") seasonNumber: Int,
            @Path("episodeNumber") episodeNumber: Int,
            @Body rating: Rating
    ): Single<Status>

    @DELETE("tv/{tvId}/season/{seasonNumber}/episode/{episodeNumber}/rating")
    fun deleteEpisodeRating(@Path("tvId") tvId: Long,
                            @Path("seasonNumber") seasonNumber: Int,
                            @Path("episodeNumber") episodeNumber: Int
    ): Single<Status>


    @GET("discover/tv")
    fun discoverTVShow(
            @Query("sort_by") sortBy: String = "popularity.desc",
            @Query("air_date.gte") minAirDate: String? = null,
            @Query("air_date.lte") maxAirDate: String? = null,
            @Query("with_genres") genreIds: String? = null,
            @Query("page") page: Int = 1
    ): Single<Results<TVShow>>
}