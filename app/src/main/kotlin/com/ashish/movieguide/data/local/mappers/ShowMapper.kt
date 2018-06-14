package com.ashish.movieguide.data.local.mappers

import com.ashish.movieguide.data.local.entities.LocalShow
import com.ashish.movieguide.data.local.entities.ShowEntity
import com.ashish.movieguide.data.remote.entities.tmdb.TVShow
import com.ashish.movieguide.data.remote.entities.trakt.TraktShow
import com.ashish.movieguide.utils.BiFunction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowMapper @Inject constructor() : BiFunction<TVShow, TraktShow, LocalShow> {

    override fun apply(var1: TVShow, var2: TraktShow?): LocalShow {
        val showEntity = ShowEntity()
        var1.apply {
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