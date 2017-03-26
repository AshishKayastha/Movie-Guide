package com.ashish.movieguide.data.database.mappers

import com.ashish.movieguide.data.database.entities.EpisodeEntity
import com.ashish.movieguide.data.network.entities.tmdb.Episode
import com.ashish.movieguide.data.network.entities.trakt.TraktEpisode
import io.reactivex.functions.BiFunction
import javax.inject.Inject
import javax.inject.Singleton
import com.ashish.movieguide.data.database.entities.Episode as LocalEpisode

@Singleton
class EpisodeMapper @Inject constructor() : BiFunction<Episode, TraktEpisode, LocalEpisode> {

    override fun apply(episode: Episode, traktEpisode: TraktEpisode?): LocalEpisode {
        val episodeEntity = EpisodeEntity()
        episode.run {
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

        traktEpisode?.addToEpisodeEntity(episodeEntity)
        return episodeEntity
    }
}