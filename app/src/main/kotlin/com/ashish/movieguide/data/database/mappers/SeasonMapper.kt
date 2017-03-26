package com.ashish.movieguide.data.database.mappers

import com.ashish.movieguide.data.database.entities.SeasonEntity
import com.ashish.movieguide.data.network.entities.tmdb.Season
import com.ashish.movieguide.data.network.entities.trakt.TraktSeason
import io.reactivex.functions.BiFunction
import javax.inject.Inject
import javax.inject.Singleton
import com.ashish.movieguide.data.database.entities.Season as LocalSeason

@Singleton
class SeasonMapper @Inject constructor() : BiFunction<Season, TraktSeason, LocalSeason> {

    override fun apply(season: Season, traktSeason: TraktSeason?): LocalSeason {
        val seasonEntity = SeasonEntity()
        season.run {
            seasonEntity.tmdbId = id!!
            seasonEntity.airDate = airDate
            seasonEntity.posterPath = posterPath
            seasonEntity.seasonNumber = seasonNumber
            seasonEntity.episodeCount = episodeCount
        }

        traktSeason?.addToSeasonEntity(seasonEntity)
        return seasonEntity
    }
}