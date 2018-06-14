package com.ashish.movieguide.data.local.mappers

import com.ashish.movieguide.data.local.entities.EpisodeEntity
import com.ashish.movieguide.data.local.entities.LocalEpisode
import com.ashish.movieguide.data.remote.entities.tmdb.Episode
import com.ashish.movieguide.data.remote.entities.trakt.TraktEpisode
import com.ashish.movieguide.utils.BiFunction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EpisodeMapper @Inject constructor() : BiFunction<Episode, TraktEpisode, LocalEpisode> {

    override fun apply(var1: Episode, var2: TraktEpisode?): LocalEpisode {
        val episodeEntity = EpisodeEntity()
        var1.apply {
            episodeEntity.tmdbId = id!!
            episodeEntity.name = name
            episodeEntity.overview = overview
            episodeEntity.airDate = airDate
            episodeEntity.stillPath = stillPath
            episodeEntity.seasonNumber = seasonNumber
            episodeEntity.voteCount = voteCount
            episodeEntity.voteAverage = voteAverage
            episodeEntity.episodeNumber = episodeNumber
        }

        var2?.addToEpisodeEntity(episodeEntity)
        return episodeEntity
    }
}