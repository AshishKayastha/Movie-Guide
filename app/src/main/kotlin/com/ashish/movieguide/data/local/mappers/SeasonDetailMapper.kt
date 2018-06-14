package com.ashish.movieguide.data.local.mappers

import com.ashish.movieguide.data.local.entities.LocalSeason
import com.ashish.movieguide.data.local.entities.SeasonEntity
import com.ashish.movieguide.data.remote.entities.common.FullDetailContent
import com.ashish.movieguide.data.remote.entities.tmdb.SeasonDetail
import com.ashish.movieguide.data.remote.entities.trakt.TraktSeason
import io.reactivex.functions.Function
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeasonDetailMapper @Inject constructor()
    : Function<FullDetailContent<SeasonDetail, TraktSeason>, LocalSeason> {

    override fun apply(fullDetailContent: FullDetailContent<SeasonDetail, TraktSeason>): LocalSeason {
        val seasonEntity = SeasonEntity()
        fullDetailContent.detailContent?.apply {
            seasonEntity.tmdbId = id!!
            seasonEntity.name = name
            seasonEntity.overview = overview
            seasonEntity.airDate = airDate
            seasonEntity.posterPath = posterPath
            seasonEntity.seasonNumber = seasonNumber
            seasonEntity.imdbId = externalIds?.imdbId

            credits?.cast?.forEach { seasonEntity.credits?.add(it.toEntity(true)) }
            credits?.crew?.forEach { seasonEntity.credits?.add(it.toEntity(false)) }

            images?.backdrops?.forEach { seasonEntity.images?.add(it.toEntity()) }
            images?.posters?.forEach { seasonEntity.images?.add(it.toEntity()) }

            videos?.results?.forEach { seasonEntity.videos?.add(it.toEntity()) }

            episodes?.forEach { seasonEntity.episodes?.add(EpisodeMapper().apply(it, null)) }
        }

        seasonEntity.omdb = fullDetailContent.omdbDetail?.toEntity()
        fullDetailContent.traktItem?.addToSeasonEntity(seasonEntity)
        return seasonEntity
    }
}