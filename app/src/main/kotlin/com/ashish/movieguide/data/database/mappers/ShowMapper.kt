package com.ashish.movieguide.data.database.mappers

import com.ashish.movieguide.data.database.entities.Show
import com.ashish.movieguide.data.database.entities.ShowEntity
import com.ashish.movieguide.data.network.entities.tmdb.TVShow
import com.ashish.movieguide.data.network.entities.trakt.TraktShow
import com.ashish.movieguide.utils.BiFunction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowMapper @Inject constructor() : BiFunction<TVShow, TraktShow, Show> {

    override fun apply(var1: TVShow, var2: TraktShow?): Show {
        val showEntity = ShowEntity()
        var1.run {
            showEntity.tmdbId = id!!
            showEntity.name = name
            showEntity.overview = overview
            showEntity.voteCount = voteCount
            showEntity.voteAverage = voteAverage
            showEntity.posterPath = posterPath
            showEntity.backdropPath = backdropPath
            showEntity.firstAirDate = firstAirDate
        }

        var2?.addToShowEntity(showEntity)
        return showEntity
    }
}