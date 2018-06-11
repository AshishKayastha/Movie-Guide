package com.ashish.movieguide.data.interactors

import com.ashish.movieguide.data.network.api.tmdb.OMDbApi
import com.ashish.movieguide.data.network.api.tmdb.TVShowApi
import com.ashish.movieguide.data.network.api.trakt.TraktShowApi
import com.ashish.movieguide.data.network.entities.common.FullDetailContent
import com.ashish.movieguide.data.network.entities.common.OMDbDetail
import com.ashish.movieguide.data.network.entities.tmdb.EpisodeDetail
import com.ashish.movieguide.data.network.entities.tmdb.Results
import com.ashish.movieguide.data.network.entities.tmdb.SeasonDetail
import com.ashish.movieguide.data.network.entities.tmdb.TVShow
import com.ashish.movieguide.data.network.entities.tmdb.TVShowDetail
import com.ashish.movieguide.data.network.entities.trakt.TraktEpisode
import com.ashish.movieguide.data.network.entities.trakt.TraktSeason
import com.ashish.movieguide.data.network.entities.trakt.TraktShow
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ashish on Dec 29.
 */
@Singleton
class TVShowInteractor @Inject constructor(
        private val tvShowApi: TVShowApi,
        private val omDbApi: OMDbApi,
        private val traktShowApi: TraktShowApi
) {

    companion object {
        private const val APPENDED_RESPONSE = "credits,external_ids,images,videos"
    }

    fun getTVShowsByType(tvShowType: String?, page: Int = 1): Single<Results<TVShow>> {
        return tvShowApi.getTVShows(tvShowType, page)
    }

    fun getFullTVShowDetail(tvId: Long): Observable<FullDetailContent<TVShowDetail, TraktShow>> {
        return tvShowApi.getTVShowDetail(tvId, "similar,$APPENDED_RESPONSE")
                .flatMap { convertToFullShowDetail(it) }
    }

    private fun convertToFullShowDetail(
            tvShowDetail: TVShowDetail
    ): Observable<FullDetailContent<TVShowDetail, TraktShow>> {
        val imdbId = tvShowDetail.externalIds?.imdbId
        return if (imdbId.isNotNullOrEmpty()) {
            val traktShowObservable = traktShowApi.getShowDetail(imdbId!!)
                    .onErrorReturnItem(TraktShow())

            val omdbDetailObservable = getOMDbDetail(imdbId)

            Observable.zip(traktShowObservable, omdbDetailObservable, BiFunction { traktShow, omDbDetail ->
                FullDetailContent(tvShowDetail, omDbDetail, traktShow)
            })
        } else {
            Observable.just(FullDetailContent(tvShowDetail))
        }
    }

    fun getFullSeasonDetail(tvId: Long, seasonNumber: Int): Observable<FullDetailContent<SeasonDetail, TraktSeason>> {
        return tvShowApi.getSeasonDetail(tvId, seasonNumber, APPENDED_RESPONSE)
                .flatMap { convertToFullSeasonDetail(it, seasonNumber) }
    }

    private fun convertToFullSeasonDetail(
            seasonDetail: SeasonDetail,
            seasonNumber: Int
    ): Observable<FullDetailContent<SeasonDetail, TraktSeason>> {
        val imdbId = seasonDetail.externalIds?.imdbId
        return if (imdbId.isNotNullOrEmpty()) {
            val traktSeasonObservable = traktShowApi.getSeasonDetail(imdbId!!, seasonNumber)
                    .onErrorReturnItem(TraktSeason())

            val omdbDetailObservable = getOMDbDetail(imdbId)

            Observable.zip(traktSeasonObservable, omdbDetailObservable, BiFunction { traktSeason, omDbDetail ->
                FullDetailContent(seasonDetail, omDbDetail, traktSeason)
            })
        } else {
            Observable.just(FullDetailContent(seasonDetail))
        }
    }

    fun getFullEpisodeDetail(
            tvId: Long,
            seasonNumber: Int,
            episodeNumber: Int
    ): Observable<FullDetailContent<EpisodeDetail, TraktEpisode>> {
        return tvShowApi.getEpisodeDetail(tvId, seasonNumber, episodeNumber, APPENDED_RESPONSE)
                .flatMap { convertToFullEpisodeDetail(it, seasonNumber, episodeNumber) }
    }

    private fun convertToFullEpisodeDetail(
            episodeDetail: EpisodeDetail,
            seasonNumber: Int,
            episodeNumber: Int
    ): Observable<FullDetailContent<EpisodeDetail, TraktEpisode>> {
        val imdbId = episodeDetail.externalIds?.imdbId
        return if (imdbId.isNotNullOrEmpty()) {
            val traktEpisodeObservable = traktShowApi.getEpisodeDetail(imdbId!!, seasonNumber, episodeNumber)
                    .onErrorReturnItem(TraktEpisode())

            val omdbDetailObservable = getOMDbDetail(imdbId)

            Observable.zip(traktEpisodeObservable, omdbDetailObservable, BiFunction { traktEpisode, omDbDetail ->
                FullDetailContent(episodeDetail, omDbDetail, traktEpisode)
            })
        } else {
            Observable.just(FullDetailContent(episodeDetail))
        }
    }

    private fun getOMDbDetail(imdbId: String): Observable<OMDbDetail> {
        return omDbApi.getOMDbDetail(imdbId).onErrorReturnItem(OMDbDetail())
    }

    fun discoverTVShow(
            sortBy: String,
            minAirDate: String?,
            maxAirDate: String?,
            genreIds: String?,
            page: Int
    ): Single<Results<TVShow>> {
        return tvShowApi.discoverTVShow(sortBy, minAirDate, maxAirDate, genreIds, page)
    }
}