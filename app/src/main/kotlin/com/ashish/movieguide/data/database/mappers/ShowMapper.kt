package com.ashish.movieguide.data.database.mappers

import com.ashish.movieguide.data.database.entities.Show
import com.ashish.movieguide.data.database.entities.ShowEntity
import com.ashish.movieguide.data.network.entities.tmdb.TVShow
import com.ashish.movieguide.data.network.entities.trakt.TraktShow
import io.reactivex.functions.BiFunction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowMapper @Inject constructor() : BiFunction<TVShow, TraktShow, Show> {

    override fun apply(tvShow: TVShow, traktShow: TraktShow?): Show {
        val showEntity = ShowEntity()
        tvShow.run {
            showEntity.tmdbId = id!!
            showEntity.name = name
            showEntity.overview = overview
            showEntity.voteCount = voteCount
            showEntity.voteAverage = voteAverage
            showEntity.posterPath = posterPath
            showEntity.backdropPath = backdropPath
            showEntity.firstAirDate = firstAirDate
        }

        traktShow?.addToShowEntity(showEntity)
        return showEntity
    }
}