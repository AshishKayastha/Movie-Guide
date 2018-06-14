package com.ashish.movieguide.data.local.mappers

import com.ashish.movieguide.data.local.entities.LocalMovie
import com.ashish.movieguide.data.local.entities.MovieEntity
import com.ashish.movieguide.data.remote.entities.tmdb.Movie
import com.ashish.movieguide.data.remote.entities.trakt.TraktMovie
import com.ashish.movieguide.utils.BiFunction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieMapper @Inject constructor() : BiFunction<Movie, TraktMovie, LocalMovie> {

    override fun apply(var1: Movie, var2: TraktMovie?): LocalMovie {
        val movieEntity = MovieEntity()
        with(var1) {
            movieEntity.tmdbId = id!!
            movieEntity.title = title
            movieEntity.overview = overview
            movieEntity.backdropPath = backdropPath
            movieEntity.posterPath = posterPath
            movieEntity.releaseDate = releaseDate
            movieEntity.voteCount = voteCount
            movieEntity.voteAverage = voteAverage
        }

        var2?.addToMovieEntity(movieEntity)
        return movieEntity
    }
}