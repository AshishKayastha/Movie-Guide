package com.ashish.movieguide.data.database.resolvers.moviedetail

import com.ashish.movieguide.data.database.entities.MovieDetailEntity
import com.ashish.movieguide.data.database.resolvers.movie.MovieGetResolver
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping

/**
 * Created by Ashish on Feb 06.
 */
class MovieDetailSQLiteTypeMapping : SQLiteTypeMapping<MovieDetailEntity>(
        MovieDetailPutResolver(),
        MovieDetailGetResolver(MovieGetResolver()),
        MovieDetailDeleteResolver()
)