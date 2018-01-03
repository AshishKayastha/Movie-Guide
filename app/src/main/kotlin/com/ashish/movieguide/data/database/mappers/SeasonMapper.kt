package com.ashish.movieguide.data.database.mappers

import com.ashish.movieguide.data.database.entities.SeasonEntity
import com.ashish.movieguide.data.network.entities.tmdb.Season
import com.ashish.movieguide.data.network.entities.trakt.TraktSeason
import com.ashish.movieguide.utils.BiFunction
import javax.inject.Inject
import javax.inject.Singleton
import com.ashish.movieguide.data.database.entities.Season as LocalSeason

@Singleton
class SeasonMapper @Inject constructor() : BiFunction<Season, TraktSeason, LocalSeason> {

    override fun apply(var1: Season, var2: TraktSeason?): LocalSeason {
        val seasonEntity = SeasonEntity()
        var1.run {
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