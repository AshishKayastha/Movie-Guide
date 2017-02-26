package com.ashish.movieguide.data.database.resolvers.movie

import com.ashish.movieguide.data.database.entities.MovieEntity
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping

/**
 * Created by Ashish on Feb 06.
 */
class MovieSQLiteTypeMapping : SQLiteTypeMapping<MovieEntity>(
        MoviePutResolver(),
        MovieGetResolver(),
        MovieDeleteResolver()
)