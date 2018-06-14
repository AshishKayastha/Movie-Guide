package com.ashish.movieguide.data.local.mappers

import com.ashish.movieguide.data.local.entities.EpisodeEntity
import com.ashish.movieguide.data.local.entities.LocalEpisode
import com.ashish.movieguide.data.remote.entities.common.FullDetailContent
import com.ashish.movieguide.data.remote.entities.tmdb.EpisodeDetail
import com.ashish.movieguide.data.remote.entities.trakt.TraktEpisode
import io.reactivex.functions.Function
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EpisodeDetailMapper @Inject constructor()
    : Function<FullDetailContent<EpisodeDetail, TraktEpisode>, LocalEpisode> {

    override fun apply(fullDetailContent: FullDetailContent<EpisodeDetail, TraktEpisode>): LocalEpisode {
        val episodeEntity = EpisodeEntity()
        fullDetailContent.detailContent?.apply {
            episodeEntity.tmdbId = id!!
            episodeEntity.name = name
            episodeEntity.overview = overview
            episodeEntity.airDate = airDate
            episodeEntity.stillPath = stillPath
            episodeEntity.voteCount = voteCount
            episodeEntity.voteAverage = voteAverage
            episodeEntity.seasonNumber = seasonNumber
            episodeEntity.episodeNumber = episodeNumber

            credits?.cast?.forEach { episodeEntity.credits?.add(it.toEntity(true)) }
            credits?.crew?.forEach { episodeEntity.credits?.add(it.toEntity(false)) }

            images?.backdrops?.forEach { episodeEntity.images?.add(it.toEntity()) }
            images?.posters?.forEach { episodeEntity.images?.add(it.toEntity()) }

            videos?.results?.forEach { episodeEntity.videos?.add(it.toEntity()) }
        }

        episodeEntity.omdb = fullDetailContent.omdbDetail?.toEntity()
        fullDetailContent.traktItem?.addToEpisodeEntity(episodeEntity)
        return episodeEntity
    }
}