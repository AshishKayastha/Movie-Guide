package com.ashish.movieguide.data.database.mappers

import com.ashish.movieguide.data.database.entities.MovieEntity
import com.ashish.movieguide.data.network.entities.tmdb.Movie
import com.ashish.movieguide.data.network.entities.trakt.TraktMovie
import com.ashish.movieguide.utils.BiFunction
import javax.inject.Inject
import javax.inject.Singleton
import com.ashish.movieguide.data.database.entities.Movie as LocalMovie

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