package com.ashish.movieguide.data.database.resolvers.moviedetail

import com.ashish.movieguide.data.database.entities.MovieDetailEntity
import com.ashish.movieguide.data.database.tables.MoviesTable
import com.pushtorefresh.storio.sqlite.operations.delete.DefaultDeleteResolver
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery

/**
 * Created by Ashish on Feb 06.
 */
class MovieDetailDeleteResolver : DefaultDeleteResolver<MovieDetailEntity>() {

    override fun mapToDeleteQuery(movieDetailEntity: MovieDetailEntity): DeleteQuery {
        return DeleteQuery.builder()
                .table(MoviesTable.TABLE_NAME)
                .where("${MoviesTable.COL_ID} = ?")
                .whereArgs(movieDetailEntity.movieEntity.id)
                .build()
    }
}