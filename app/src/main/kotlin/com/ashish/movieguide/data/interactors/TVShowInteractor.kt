package com.ashish.movieguide.data.interactors

import com.ashish.movieguide.data.api.OMDbApi
import com.ashish.movieguide.data.api.TVShowApi
import com.ashish.movieguide.data.models.EpisodeDetail
import com.ashish.movieguide.data.models.FullDetailContent
import com.ashish.movieguide.data.models.Rating
import com.ashish.movieguide.data.models.Results
import com.ashish.movieguide.data.models.SeasonDetail
import com.ashish.movieguide.data.models.Status
import com.ashish.movieguide.data.models.TVShow
import com.ashish.movieguide.data.models.TVShowDetail
import com.ashish.movieguide.utils.extensions.convertToFullDetailContent
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ashish on Dec 29.
 */
@Singleton
class TVShowInteractor @Inject constructor(
        private val tvShowApi: TVShowApi,
        private val omDbApi: OMDbApi
) {

    companion object {
        private const val APPENDED_RESPONSE = "credits,external_ids,images,videos"
    }

    fun getTVShowsByType(tvShowType: String?, page: Int = 1): Single<Results<TVShow>> {
        return tvShowApi.getTVShows(tvShowType, page)
    }

    fun getFullTVShowDetail(tvId: Long): Single<FullDetailContent<TVShowDetail>> {
        return tvShowApi.getTVShowDetail(tvId, "similar,account_states," + APPENDED_RESPONSE)
                .flatMap { omDbApi.convertToFullDetailContent(it.externalIds?.imdbId, it) }
    }

    fun rateTVShow(movieId: Long, value: Double): Single<Status> {
        return tvShowApi.rateTVShow(movieId, Rating(value))
    }

    fun deleteTVRating(tvId: Long): Single<Status> {
        return tvShowApi.deleteTVRating(tvId)
    }

    fun getFullSeasonDetail(tvId: Long, seasonNumber: Int): Single<FullDetailContent<SeasonDetail>> {
        return tvShowApi.getSeasonDetail(tvId, seasonNumber, APPENDED_RESPONSE)
                .flatMap { omDbApi.convertToFullDetailContent(it.externalIds?.imdbId, it) }
    }

    fun getFullEpisodeDetail(tvId: Long, seasonNumber: Int, episodeNumber: Int)
            : Single<FullDetailContent<EpisodeDetail>> {
        return tvShowApi.getEpisodeDetail(tvId, seasonNumber, episodeNumber, "account_states" + APPENDED_RESPONSE)
                .flatMap { omDbApi.convertToFullDetailContent(it.externalIds?.imdbId, it) }
    }

    fun rateEpisode(tvId: Long, seasonNumber: Int, episodeNumber: Int, value: Double): Single<Status> {
        val rating = Rating(value)
        return tvShowApi.rateEpisode(tvId, seasonNumber, episodeNumber, rating)
    }

    fun deleteEpisodeRating(tvId: Long, seasonNumber: Int, episodeNumber: Int): Single<Status> {
        return tvShowApi.deleteEpisodeRating(tvId, seasonNumber, episodeNumber)
    }

    fun discoverTVShow(sortBy: String, minAirDate: String?, maxAirDate: String?, genreIds: String?,
                       page: Int): Single<Results<TVShow>> {
        return tvShowApi.discoverTVShow(sortBy, minAirDate, maxAirDate, genreIds, page)
    }
}