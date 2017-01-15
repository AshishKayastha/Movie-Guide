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
import com.ashish.movies.utils.extensions.observeOnMainThread
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ashish on Dec 29.
 */
@Singleton
class TVShowInteractor @Inject constructor(private val tvShowApi: TVShowApi, private val omDbApi: OMDbApi) {

    fun getTVShowsByType(tvShowType: String?, page: Int = 1): Observable<Results<TVShow>> {
        return tvShowApi.getTVShows(tvShowType, page).observeOnMainThread()
    }

    fun getFullTVShowDetail(tvId: Long): Observable<FullDetailContent<TVShowDetail>> {
        return tvShowApi.getTVShowDetail(tvId, "credits,similar,external_ids,images,videos")
                .flatMap { omDbApi.convertToFullDetailContent(it.externalIds?.imdbId, it) }
                .observeOnMainThread()
    }

    fun getFullSeasonDetail(tvId: Long, seasonNumber: Int): Observable<FullDetailContent<SeasonDetail>> {
        return tvShowApi.getSeasonDetail(tvId, seasonNumber, "credits,external_ids,images,videos")
                .flatMap { omDbApi.convertToFullDetailContent(it.externalIds?.imdbId, it) }
                .observeOnMainThread()
    }

    fun getFullEpisodeDetail(tvId: Long, seasonNumber: Int, episodeNumber: Int)
            : Observable<FullDetailContent<EpisodeDetail>> {
        return tvShowApi.getEpisodeDetail(tvId, seasonNumber, episodeNumber, "credits,external_ids,images,videos")
                .flatMap { omDbApi.convertToFullDetailContent(it.externalIds?.imdbId, it) }
                .observeOnMainThread()
    }

    fun discoverTVShow(sortBy: String, year: Int, genres: String? = null, page: Int): Observable<Results<TVShow>> {
        return tvShowApi.discoverTVShow(sortBy, year, genres, page).observeOnMainThread()
    }
}