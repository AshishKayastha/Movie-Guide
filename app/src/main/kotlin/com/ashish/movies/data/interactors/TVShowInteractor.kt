package com.ashish.movies.data.interactors

import com.ashish.movies.data.api.TVShowApi
import com.ashish.movies.data.models.EpisodeDetail
import com.ashish.movies.data.models.Results
import com.ashish.movies.data.models.SeasonDetail
import com.ashish.movies.data.models.TVShow
import com.ashish.movies.data.models.TVShowDetail
import com.ashish.movies.utils.ApiConstants.CREDITS_AND_SIMILAR
import com.ashish.movies.utils.extensions.observeOnMainThread
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ashish on Dec 29.
 */
@Singleton
class TVShowInteractor @Inject constructor(val tvShowApi: TVShowApi) {

    fun getTVShowsByType(tvShowType: String?, page: Int = 1): Observable<Results<TVShow>> {
        return tvShowApi.getTVShows(tvShowType, page).observeOnMainThread()
    }

    fun getTVShowDetailWithCreditsAndSimilarTVShows(tvId: Long): Observable<TVShowDetail> {
        return tvShowApi.getTVShowDetailWithAppendedResponse(tvId, CREDITS_AND_SIMILAR + ",external_ids")
                .observeOnMainThread()
    }

    fun getSeasonDetail(tvId: Long, seasonNumber: Int): Observable<SeasonDetail> {
        return tvShowApi.getSeasonDetail(tvId, seasonNumber, "credits,external_ids")
                .observeOnMainThread()
    }

    fun getEpisodeDetail(tvId: Long, seasonNumber: Int, episodeNumber: Int): Observable<EpisodeDetail> {
        return tvShowApi.getEpisodeDetail(tvId, seasonNumber, episodeNumber, "credits,external_ids")
                .observeOnMainThread()
    }

    fun discoverTVShow(sortBy: String, year: Int, genres: String? = null, page: Int): Observable<Results<TVShow>> {
        return tvShowApi.discoverTVShow(sortBy, year, genres, page).observeOnMainThread()
    }
}