package com.ashish.movies.data.database.resolvers.movie

import android.content.ContentValues
import com.ashish.movies.data.database.entities.MovieEntity
import com.ashish.movies.data.database.tables.MoviesTable
import com.pushtorefresh.storio.sqlite.operations.put.DefaultPutResolver
import com.pushtorefresh.storio.sqlite.queries.InsertQuery
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery

/**
 * Created by Ashish on Feb 05.
 */
class MoviePutResolver : DefaultPutResolver<MovieEntity>() {

    override fun mapToInsertQuery(movieEntity: MovieEntity): InsertQuery {
        return InsertQuery.builder()
                .table(MoviesTable.TABLE_NAME)
                .build()
    }

    override fun mapToUpdateQuery(movieEntity: MovieEntity): UpdateQuery {
        return UpdateQuery.builder()
                .table(MoviesTable.TABLE_NAME)
                .where("${MoviesTable.COL_ID} = ?")
                .whereArgs(movieEntity.id)
                .build()
    }

    override fun mapToContentValues(movieEntity: MovieEntity): ContentValues {
        val values = ContentValues()
        with(movieEntity) {
            values.apply {
                put(MoviesTable.COL_ID, id)
                put(MoviesTable.COL_TITLE, title)
                put(MoviesTable.COL_OVERVIEW, overview)
                put(MoviesTable.COL_BUDGET, budget)
                put(MoviesTable.COL_REVENUE, revenue)
                put(MoviesTable.COL_RUNTIME, runtime)
                put(MoviesTable.COL_STATUS, status)
                put(MoviesTable.COL_TAGLINE, tagline)
                put(MoviesTable.COL_IMDB_ID, imdbId)
                put(MoviesTable.COL_VOTE_COUNT, voteCount)
                put(MoviesTable.COL_POSTER_PATH, posterPath)
                put(MoviesTable.COL_RELEASE_DATE, releaseDate)
                put(MoviesTable.COL_VOTE_AVERAGE, voteAverage)
                put(MoviesTable.COL_BACKDROP_PATH, backdropPath)
            }
        }

        return values
    }
}