package com.ashish.movieguide.data.database.mappers

import com.ashish.movieguide.data.database.entities.EpisodeEntity
import com.ashish.movieguide.data.network.entities.tmdb.Episode
import com.ashish.movieguide.data.network.entities.trakt.TraktEpisode
import com.ashish.movieguide.utils.BiFunction
import javax.inject.Inject
import javax.inject.Singleton
import com.ashish.movieguide.data.database.entities.Episode as LocalEpisode

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