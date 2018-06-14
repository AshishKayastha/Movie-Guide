package com.ashish.movieguide.data.local.datasource

import com.ashish.movieguide.data.local.entities.LocalMovie
import com.ashish.movieguide.data.local.entities.MovieEntity
import com.ashish.movieguide.data.local.mappers.MovieDetailMapper
import com.ashish.movieguide.data.local.mappers.MovieMapper
import com.ashish.movieguide.data.remote.entities.common.FullDetailContent
import com.ashish.movieguide.data.remote.entities.tmdb.Movie
import com.ashish.movieguide.data.remote.entities.tmdb.MovieDetail
import com.ashish.movieguide.data.remote.entities.trakt.TraktMovie
import io.reactivex.Observable
import io.requery.Persistable
import io.requery.reactivex.KotlinReactiveEntityStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieLocalDataSource @Inject constructor(
        private val movieMapper: MovieMapper,
        private val movieDetailMapper: MovieDetailMapper,
        private val dataStore: KotlinReactiveEntityStore<Persistable>,
        private val personLocalDataSource: PersonLocalDataSource
) {

    fun getMovie(movieId: Long): Observable<LocalMovie> {
        return dataStore.select(LocalMovie::class)
                .where(MovieEntity.TMDB_ID.eq(movieId))
                .get()
                .observable()
    }

    private fun putMovieBlocking(movie: Movie, traktMovie: TraktMovie?): LocalMovie {
        //        val savedMovie = dataStore.findByKey(LocalMovie::class, movie.id).blockingGet()
        //        if (savedMovie != null) {
        //            savedMovie.images?.forEach {
        //
        //            }
        //        }

        val movieEntity = movieMapper.apply(movie, traktMovie)
        return dataStore.upsert(movieEntity).blockingGet()
    }

    fun putMovieDetailBlocking(fullMovieDetail: FullDetailContent<MovieDetail, TraktMovie>): LocalMovie {
        //        val movieDetail = fullMovieDetail.detailContent
        //        val similarMovies = movieDetail?.similarMovieResults?.results
        //        similarMovies?.forEach { putMovieBlocking(it, null) }
        //
        //        val credits = movieDetail?.credits
        //        credits?.cast?.forEach { credit ->
        //            localPersonRepository.putPersonBlocking(credit.toPerson(), null)
        //        }
        //
        //        credits?.crew?.forEach { credit ->
        //            localPersonRepository.putPersonBlocking(credit.toPerson(), null)
        //        }

        val movieDetailEntity = movieDetailMapper.apply(fullMovieDetail)
        return dataStore.upsert(movieDetailEntity).blockingGet()
    }

    private fun putMovie(movie: Movie, traktMovie: TraktMovie?): Observable<LocalMovie> {
        val movieEntity = movieMapper.apply(movie, traktMovie)
        return dataStore.upsert(movieEntity).toObservable()
    }

    fun putMovieDetail(fullMovieDetail: FullDetailContent<MovieDetail, TraktMovie>): Observable<LocalMovie> {
        //        val movieDetail = fullMovieDetail.detailContent
        //        val similarMovies = movieDetail?.similarMovieResults?.results
        //        similarMovies?.forEach { putMovie(it, null) }
        //
        //        val credits = movieDetail?.credits
        //        credits?.cast?.forEach { credit ->
        //            localPersonRepository.putPerson(credit.toPerson(), null)
        //        }
        //
        //        credits?.crew?.forEach { credit ->
        //            localPersonRepository.putPerson(credit.toPerson(), null)
        //        }

        val movieDetailEntity = movieDetailMapper.apply(fullMovieDetail)
        return dataStore.upsert(movieDetailEntity).toObservable()
    }
}