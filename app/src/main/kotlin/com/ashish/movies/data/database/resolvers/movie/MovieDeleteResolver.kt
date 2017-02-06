package com.ashish.movies.data.database.resolvers.movie

import com.ashish.movies.data.database.entities.MovieEntity
import com.ashish.movies.data.database.tables.MoviesTable
import com.pushtorefresh.storio.sqlite.operations.delete.DefaultDeleteResolver
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery

/**
 * Created by Ashish on Feb 06.
 */
class MovieDeleteResolver : DefaultDeleteResolver<MovieEntity>() {

    override fun mapToDeleteQuery(movieEntity: MovieEntity): DeleteQuery {
        return DeleteQuery.builder()
                .table(MoviesTable.TABLE_NAME)
                .where("${MoviesTable.COL_ID} = ?")
                .whereArgs(movieEntity.id)
                .build()
    }
}