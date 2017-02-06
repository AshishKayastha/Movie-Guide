package com.ashish.movies.data.database.entities

import com.ashish.movies.data.database.tables.SimilarMoviesTable.COL_BACKDROP_PATH
import com.ashish.movies.data.database.tables.SimilarMoviesTable.COL_ID
import com.ashish.movies.data.database.tables.SimilarMoviesTable.COL_MEDIA_ID
import com.ashish.movies.data.database.tables.SimilarMoviesTable.COL_OVERVIEW
import com.ashish.movies.data.database.tables.SimilarMoviesTable.COL_POSTER_PATH
import com.ashish.movies.data.database.tables.SimilarMoviesTable.COL_RELEASE_DATE
import com.ashish.movies.data.database.tables.SimilarMoviesTable.COL_TITLE
import com.ashish.movies.data.database.tables.SimilarMoviesTable.COL_VOTE_AVERAGE
import com.ashish.movies.data.database.tables.SimilarMoviesTable.COL_VOTE_COUNT
import com.ashish.movies.data.database.tables.SimilarMoviesTable.TABLE_NAME
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteCreator
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType

/**
 * Created by Ashish on Feb 06.
 */
@StorIOSQLiteType(table = TABLE_NAME)
data class SimilarMovieEntity @StorIOSQLiteCreator constructor(
        @get:StorIOSQLiteColumn(name = COL_ID, key = true) val id: Long,
        @get:StorIOSQLiteColumn(name = COL_MEDIA_ID) val mediaId: Long,
        @get:StorIOSQLiteColumn(name = COL_TITLE) val title: String? = null,
        @get:StorIOSQLiteColumn(name = COL_OVERVIEW) val overview: String? = null,
        @get:StorIOSQLiteColumn(name = COL_VOTE_COUNT) val voteCount: Int? = null,
        @get:StorIOSQLiteColumn(name = COL_POSTER_PATH) val posterPath: String? = null,
        @get:StorIOSQLiteColumn(name = COL_RELEASE_DATE) val releaseDate: String? = null,
        @get:StorIOSQLiteColumn(name = COL_VOTE_AVERAGE) val voteAverage: Double? = null,
        @get:StorIOSQLiteColumn(name = COL_BACKDROP_PATH) val backdropPath: String? = null
)