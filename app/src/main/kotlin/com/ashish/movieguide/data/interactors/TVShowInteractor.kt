package com.ashish.movieguide.data.interactors

import com.ashish.movieguide.data.api.tmdb.OMDbApi
import com.ashish.movieguide.data.api.tmdb.TVShowApi
import com.ashish.movieguide.data.api.trakt.TraktShowApi
import com.ashish.movieguide.data.models.common.FullDetailContent
import com.ashish.movieguide.data.models.common.OMDbDetail
import com.ashish.movieguide.data.models.tmdb.EpisodeDetail
import com.ashish.movieguide.data.models.tmdb.Results
import com.ashish.movieguide.data.models.tmdb.SeasonDetail
import com.ashish.movieguide.data.models.tmdb.TVShow
import com.ashish.movieguide.data.models.tmdb.TVShowDetail
import com.ashish.movieguide.data.models.trakt.TraktEpisode
import com.ashish.movieguide.data.models.trakt.TraktSeason
import com.ashish.movieguide.data.models.trakt.TraktShow
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
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

    fun getFullTVShowDetail(tvId: Long): Single<FullDetailContent<TVShowDetail, TraktShow>> {
        return tvShowApi.getTVShowDetail(tvId, "similar," + APPENDED_RESPONSE)
                .flatMap { convertToFullShowDetail(it) }
    }

    private fun convertToFullShowDetail(tvShowDetail: TVShowDetail)
            : Single<FullDetailContent<TVShowDetail, TraktShow>> {
        val imdbId = tvShowDetail.externalIds?.imdbId
        if (imdbId.isNotNullOrEmpty()) {
            val traktShowSingle = traktShowApi.getShowDetail(imdbId!!)
                    .onErrorReturnItem(TraktShow())

            val omdbDetailSingle = getOMDbSingle(imdbId)

            return Single.zip(traktShowSingle, omdbDetailSingle, BiFunction { traktShow, omDbDetail ->
                FullDetailContent(tvShowDetail, omDbDetail, traktShow)
            })
        } else {
            return Single.just(FullDetailContent(tvShowDetail))
        }
    }

    fun getFullSeasonDetail(tvId: Long, seasonNumber: Int): Single<FullDetailContent<SeasonDetail, TraktSeason>> {
        return tvShowApi.getSeasonDetail(tvId, seasonNumber, APPENDED_RESPONSE)
                .flatMap { convertToFullSeasonDetail(it, seasonNumber) }
    }

    private fun convertToFullSeasonDetail(seasonDetail: SeasonDetail, seasonNumber: Int)
            : Single<FullDetailContent<SeasonDetail, TraktSeason>> {
        val imdbId = seasonDetail.externalIds?.imdbId
        if (imdbId.isNotNullOrEmpty()) {
            val traktSeasonSingle = traktShowApi.getSeasonDetail(imdbId!!, seasonNumber)
                    .onErrorReturnItem(TraktSeason())

            val omdbDetailSingle = getOMDbSingle(imdbId)

            return Single.zip(traktSeasonSingle, omdbDetailSingle, BiFunction { traktSeason, omDbDetail ->
                FullDetailContent(seasonDetail, omDbDetail, traktSeason)
            })
        } else {
            return Single.just(FullDetailContent(seasonDetail))
        }
    }

    fun getFullEpisodeDetail(tvId: Long, seasonNumber: Int, episodeNumber: Int)
            : Single<FullDetailContent<EpisodeDetail, TraktEpisode>> {
        return tvShowApi.getEpisodeDetail(tvId, seasonNumber, episodeNumber, APPENDED_RESPONSE)
                .flatMap { convertToFullEpisodeDetail(it, seasonNumber, episodeNumber) }
    }

    private fun convertToFullEpisodeDetail(episodeDetail: EpisodeDetail, seasonNumber: Int,
                                           episodeNumber: Int)
            : Single<FullDetailContent<EpisodeDetail, TraktEpisode>> {
        val imdbId = episodeDetail.externalIds?.imdbId
        if (imdbId.isNotNullOrEmpty()) {
            val traktEpisodeSingle = traktShowApi.getEpisodeDetail(imdbId!!, seasonNumber, episodeNumber)
                    .onErrorReturnItem(TraktEpisode())

            val omdbDetailSingle = getOMDbSingle(imdbId)

            return Single.zip(traktEpisodeSingle, omdbDetailSingle, BiFunction { traktEpisode, omDbDetail ->
                FullDetailContent(episodeDetail, omDbDetail, traktEpisode)
            })
        } else {
            return Single.just(FullDetailContent(episodeDetail))
        }
    }

    private fun getOMDbSingle(imdbId: String) = omDbApi.getDetailFromIMDbId(imdbId)
            .onErrorReturnItem(OMDbDetail())

    fun discoverTVShow(sortBy: String, minAirDate: String?, maxAirDate: String?, genreIds: String?,
                       page: Int): Single<Results<TVShow>> {
        return tvShowApi.discoverTVShow(sortBy, minAirDate, maxAirDate, genreIds, page)
    }
}