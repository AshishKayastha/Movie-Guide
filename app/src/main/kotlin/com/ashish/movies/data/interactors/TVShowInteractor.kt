package com.ashish.movies.data.interactors

import com.ashish.movies.data.api.OMDbApi
import com.ashish.movies.data.api.TVShowApi
import com.ashish.movies.data.models.EpisodeDetail
import com.ashish.movies.data.models.FullDetailContent
import com.ashish.movies.data.models.Results
import com.ashish.movies.data.models.SeasonDetail
import com.ashish.movies.data.models.TVShow
import com.ashish.movies.data.models.TVShowDetail
import com.ashish.movies.utils.extensions.convertToFullDetailContent
import io.reactivex.Observable
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

    fun getTVShowsByType(tvShowType: String?, page: Int = 1): Observable<Results<TVShow>> {
        return tvShowApi.getTVShows(tvShowType, page)
    }

    fun getFullTVShowDetail(tvId: Long): Observable<FullDetailContent<TVShowDetail>> {
        return tvShowApi.getTVShowDetail(tvId, "similar," + APPENDED_RESPONSE)
                .flatMap { omDbApi.convertToFullDetailContent(it.externalIds?.imdbId, it) }
    }

    fun getFullSeasonDetail(tvId: Long, seasonNumber: Int): Observable<FullDetailContent<SeasonDetail>> {
        return tvShowApi.getSeasonDetail(tvId, seasonNumber, APPENDED_RESPONSE)
                .flatMap { omDbApi.convertToFullDetailContent(it.externalIds?.imdbId, it) }
    }

    fun getFullEpisodeDetail(tvId: Long, seasonNumber: Int, episodeNumber: Int)
            : Observable<FullDetailContent<EpisodeDetail>> {
        return tvShowApi.getEpisodeDetail(tvId, seasonNumber, episodeNumber, APPENDED_RESPONSE)
                .flatMap { omDbApi.convertToFullDetailContent(it.externalIds?.imdbId, it) }
    }

    fun discoverTVShow(sortBy: String, minAirDate: String?, maxAirDate: String?, genreIds: String?,
                       page: Int): Observable<Results<TVShow>> {
        return tvShowApi.discoverTVShow(sortBy, minAirDate, maxAirDate, genreIds, page)
    }
}