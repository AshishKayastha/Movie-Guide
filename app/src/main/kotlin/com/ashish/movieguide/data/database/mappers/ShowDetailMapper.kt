package com.ashish.movieguide.data.database.mappers

import com.ashish.movieguide.data.database.entities.Show
import com.ashish.movieguide.data.database.entities.ShowEntity
import com.ashish.movieguide.data.database.entities.SimilarContentEntity
import com.ashish.movieguide.data.network.entities.common.FullDetailContent
import com.ashish.movieguide.data.network.entities.tmdb.TVShowDetail
import com.ashish.movieguide.data.network.entities.trakt.TraktShow
import com.ashish.movieguide.utils.extensions.convertListToCommaSeparatedText
import io.reactivex.functions.Function
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowDetailMapper @Inject constructor() : Function<FullDetailContent<TVShowDetail, TraktShow>, Show> {

    override fun apply(fullDetailContent: FullDetailContent<TVShowDetail, TraktShow>): Show {
        val showEntity = ShowEntity()
        fullDetailContent.detailContent?.run {
            showEntity.tmdbId = id!!
            showEntity.name = name
            showEntity.type = type
            showEntity.status = status
            showEntity.overview = overview
            showEntity.homepage = homepage
            showEntity.voteCount = voteCount
            showEntity.voteAverage = voteAverage
            showEntity.posterPath = posterPath
            showEntity.backdropPath = backdropPath
            showEntity.firstAirDate = firstAirDate
            showEntity.lastAirDate = lastAirDate
            showEntity.numberOfSeasons = numberOfSeasons
            showEntity.numberOfEpisodes = numberOfEpisodes

            showEntity.networks = networks.convertListToCommaSeparatedText { it.name.toString() }

            credits?.cast?.forEach { showEntity.credits?.add(it.toEntity(true)) }
            credits?.crew?.forEach { showEntity.credits?.add(it.toEntity(false)) }

            genres?.forEach { showEntity.genres?.add(it.toEntity()) }

            images?.backdrops?.forEach { showEntity.images?.add(it.toEntity()) }
            images?.posters?.forEach { showEntity.images?.add(it.toEntity()) }

            videos?.results?.forEach { showEntity.videos?.add(it.toEntity()) }

            seasons?.forEach { showEntity.seasons?.add(SeasonMapper().apply(it, null)) }

            similarTVShowResults?.results?.forEach {
                val similarShow = SimilarContentEntity()
                similarShow.mediaId = id
                showEntity.similarShows?.add(similarShow)
            }
        }

        showEntity.omdb = fullDetailContent.omdbDetail?.toEntity()
        fullDetailContent.traktItem?.addToShowEntity(showEntity)
        return showEntity
    }
}