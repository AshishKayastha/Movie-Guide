package com.ashish.movieguide.data.database.mappers

import com.ashish.movieguide.data.database.entities.MovieEntity
import com.ashish.movieguide.data.network.entities.tmdb.Movie
import com.ashish.movieguide.data.network.entities.trakt.TraktMovie
import io.reactivex.functions.BiFunction
import javax.inject.Inject
import javax.inject.Singleton
import com.ashish.movieguide.data.database.entities.Movie as LocalMovie

@Singleton
class MovieMapper @Inject constructor() : BiFunction<Movie, TraktMovie, LocalMovie> {

    override fun apply(movie: Movie, traktMovie: TraktMovie?): LocalMovie {
        val movieEntity = MovieEntity()
        with(movie) {
            movieEntity.tmdbId = id!!
            movieEntity.title = title
            movieEntity.overview = overview
            movieEntity.backdropPath = backdropPath
            movieEntity.posterPath = posterPath
            movieEntity.releaseDate = releaseDate
            movieEntity.voteCount = voteCount
            movieEntity.voteAverage = voteAverage
        }

        traktMovie?.addToMovieEntity(movieEntity)
        return movieEntity
    }
}