package com.ashish.movieguide.data.database.mappers

import com.ashish.movieguide.data.database.entities.Movie
import com.ashish.movieguide.data.database.entities.MovieEntity
import com.ashish.movieguide.data.database.entities.SimilarContentEntity
import com.ashish.movieguide.data.network.entities.common.FullDetailContent
import com.ashish.movieguide.data.network.entities.tmdb.MovieDetail
import com.ashish.movieguide.data.network.entities.trakt.TraktMovie
import io.reactivex.functions.Function
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieDetailMapper @Inject constructor() : Function<FullDetailContent<MovieDetail, TraktMovie>, Movie> {

    override fun apply(fullDetailContent: FullDetailContent<MovieDetail, TraktMovie>): Movie {
        val movieEntity = MovieEntity()
        fullDetailContent.detailContent?.run {
            movieEntity.tmdbId = id!!
            movieEntity.title = title
            movieEntity.overview = overview
            movieEntity.backdropPath = backdropPath
            movieEntity.posterPath = posterPath
            movieEntity.releaseDate = releaseDate
            movieEntity.voteCount = voteCount
            movieEntity.voteAverage = voteAverage
            movieEntity.budget = budget
            movieEntity.runtime = runtime
            movieEntity.revenue = revenue
            movieEntity.status = status
            movieEntity.tagline = tagline

            credits?.cast?.forEach { movieEntity.credits?.add(it.toEntity(true)) }
            credits?.crew?.forEach { movieEntity.credits?.add(it.toEntity(false)) }

            genres?.forEach { movieEntity.genres?.add(it.toEntity()) }

            images?.backdrops?.forEach { movieEntity.images?.add(it.toEntity()) }
            images?.posters?.forEach { movieEntity.images?.add(it.toEntity()) }

            videos?.results?.forEach { movieEntity.videos?.add(it.toEntity()) }

            similarMovieResults?.results?.forEach {
                val similarMovie = SimilarContentEntity()
                similarMovie.mediaId = id
                movieEntity.similarMovies?.add(similarMovie)
            }
        }

        movieEntity.omdb = fullDetailContent.omdbDetail?.toEntity()
        fullDetailContent.traktItem?.addToMovieEntity(movieEntity)
        return movieEntity
    }
}