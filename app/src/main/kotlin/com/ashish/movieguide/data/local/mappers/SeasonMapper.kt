package com.ashish.movieguide.data.local.mappers

import com.ashish.movieguide.data.local.entities.LocalSeason
import com.ashish.movieguide.data.local.entities.SeasonEntity
import com.ashish.movieguide.data.remote.entities.tmdb.Season
import com.ashish.movieguide.data.remote.entities.trakt.TraktSeason
import com.ashish.movieguide.utils.BiFunction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeasonMapper @Inject constructor() : BiFunction<Season, TraktSeason, LocalSeason> {

    override fun apply(var1: Season, var2: TraktSeason?): LocalSeason {
        val seasonEntity = SeasonEntity()
        var1.apply {
            seasonEntity.tmdbId = id!!
            seasonEntity.airDate = airDate
            seasonEntity.posterPath = posterPath
            seasonEntity.seasonNumber = seasonNumber
            seasonEntity.episodeCount = episodeCount
        }

        var2?.addToSeasonEntity(seasonEntity)
        return seasonEntity
    }
}