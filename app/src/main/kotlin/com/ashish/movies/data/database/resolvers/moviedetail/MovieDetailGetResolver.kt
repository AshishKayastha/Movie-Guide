package com.ashish.movies.data.database.resolvers.moviedetail

import android.database.Cursor
import com.ashish.movies.data.database.entities.CreditEntity
import com.ashish.movies.data.database.entities.GenreEntity
import com.ashish.movies.data.database.entities.ImageEntity
import com.ashish.movies.data.database.entities.MovieDetailEntity
import com.ashish.movies.data.database.entities.SimilarMovieEntity
import com.ashish.movies.data.database.entities.VideoEntity
import com.ashish.movies.data.database.getContentList
import com.ashish.movies.data.database.getOMDbEntity
import com.ashish.movies.data.database.resolvers.movie.MovieGetResolver
import com.ashish.movies.data.database.tables.CreditsTable
import com.ashish.movies.data.database.tables.GenresTable
import com.ashish.movies.data.database.tables.ImagesTable
import com.ashish.movies.data.database.tables.SimilarMoviesTable
import com.ashish.movies.data.database.tables.VideosTable
import com.pushtorefresh.storio.sqlite.StorIOSQLite
import com.pushtorefresh.storio.sqlite.operations.get.GetResolver
import com.pushtorefresh.storio.sqlite.queries.Query
import com.pushtorefresh.storio.sqlite.queries.RawQuery
import java.util.*

/**
 * Created by Ashish on Feb 05.
 */
class MovieDetailGetResolver(private val movieGetResolver: MovieGetResolver) : GetResolver<MovieDetailEntity>() {

    private val storIOSQLiteFromPerformGet = ThreadLocal<StorIOSQLite>()

    override fun mapFromCursor(cursor: Cursor): MovieDetailEntity {
        val storIOSQLite = storIOSQLiteFromPerformGet.get()
        val movieEntity = movieGetResolver.mapFromCursor(cursor)

        val movieId = movieEntity.id

        val omdbEntity = storIOSQLite.getOMDbEntity(movieId)
        val genreList = storIOSQLite.getContentList<GenreEntity>(movieId, GenresTable.TABLE_NAME)
        val imageList = storIOSQLite.getContentList<ImageEntity>(movieId, ImagesTable.TABLE_NAME)
        val videoList = storIOSQLite.getContentList<VideoEntity>(movieId, VideosTable.TABLE_NAME)
        val creditList = storIOSQLite.getContentList<CreditEntity>(movieId, CreditsTable.TABLE_NAME)
        val similarMoviesList = storIOSQLite.getContentList<SimilarMovieEntity>(movieId, SimilarMoviesTable.TABLE_NAME)

        val castList = ArrayList<CreditEntity>()
        val crewList = ArrayList<CreditEntity>()

        creditList?.forEach {
            if (CreditsTable.CREDIT_TYE_CAST == it.creditType) {
                castList.add(it)
            } else {
                crewList.add(it)
            }
        }

        return MovieDetailEntity(movieEntity, omdbEntity, genreList, crewList, castList, imageList,
                videoList, similarMoviesList)
    }

    override fun performGet(storIOSQLite: StorIOSQLite, rawQuery: RawQuery): Cursor {
        storIOSQLiteFromPerformGet.set(storIOSQLite)
        return storIOSQLite.lowLevel().rawQuery(rawQuery)
    }

    override fun performGet(storIOSQLite: StorIOSQLite, query: Query): Cursor {
        storIOSQLiteFromPerformGet.set(storIOSQLite)
        return storIOSQLite.lowLevel().query(query)
    }
}