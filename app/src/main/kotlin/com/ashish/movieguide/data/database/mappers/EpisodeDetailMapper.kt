package com.ashish.movieguide.data.database.mappers

import com.ashish.movieguide.data.database.entities.Episode
import com.ashish.movieguide.data.database.entities.EpisodeEntity
import com.ashish.movieguide.data.network.entities.common.FullDetailContent
import com.ashish.movieguide.data.network.entities.tmdb.EpisodeDetail
import com.ashish.movieguide.data.network.entities.trakt.TraktEpisode
import io.reactivex.functions.Function
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EpisodeDetailMapper @Inject constructor() : Function<FullDetailContent<EpisodeDetail, TraktEpisode>, Episode> {

    override fun apply(fullDetailContent: FullDetailContent<EpisodeDetail, TraktEpisode>): Episode {
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