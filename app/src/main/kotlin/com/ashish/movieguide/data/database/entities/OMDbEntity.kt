package com.ashish.movieguide.data.database.entities

import com.ashish.movieguide.data.database.tables.OMDbTable.COL_AWARDS
import com.ashish.movieguide.data.database.tables.OMDbTable.COL_COUNTRY
import com.ashish.movieguide.data.database.tables.OMDbTable.COL_IMDB_ID
import com.ashish.movieguide.data.database.tables.OMDbTable.COL_IMDB_RATING
import com.ashish.movieguide.data.database.tables.OMDbTable.COL_IMDB_VOTES
import com.ashish.movieguide.data.database.tables.OMDbTable.COL_LANGUAGE
import com.ashish.movieguide.data.database.tables.OMDbTable.COL_MEDIA_ID
import com.ashish.movieguide.data.database.tables.OMDbTable.COL_METASCORE
import com.ashish.movieguide.data.database.tables.OMDbTable.COL_PRODUCTION
import com.ashish.movieguide.data.database.tables.OMDbTable.COL_RATED
import com.ashish.movieguide.data.database.tables.OMDbTable.COL_TOMATO_IMAGE
import com.ashish.movieguide.data.database.tables.OMDbTable.COL_TOMATO_METER
import com.ashish.movieguide.data.database.tables.OMDbTable.COL_TOMATO_RATING
import com.ashish.movieguide.data.database.tables.OMDbTable.COL_TOMATO_URL
import com.ashish.movieguide.data.database.tables.OMDbTable.COL_TOMATO_USER_METER
import com.ashish.movieguide.data.database.tables.OMDbTable.COL_TOMATO_USER_RATING
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteCreator
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType

/**
 * Created by Ashish on Feb 06.
 */
@StorIOSQLiteType(table = "omdb_contents")
data class OMDbEntity @StorIOSQLiteCreator constructor(
        @get:StorIOSQLiteColumn(name = COL_IMDB_ID, key = true) val imdbId: String,
        @get:StorIOSQLiteColumn(name = COL_MEDIA_ID) val mediaId: Long,
        @get:StorIOSQLiteColumn(name = COL_RATED) val rated: String? = null,
        @get:StorIOSQLiteColumn(name = COL_COUNTRY) val country: String? = null,
        @get:StorIOSQLiteColumn(name = COL_AWARDS) val awards: String? = null,
        @get:StorIOSQLiteColumn(name = COL_LANGUAGE) val language: String? = null,
        @get:StorIOSQLiteColumn(name = COL_METASCORE) val metascore: String? = null,
        @get:StorIOSQLiteColumn(name = COL_IMDB_RATING) val imdbRating: String? = null,
        @get:StorIOSQLiteColumn(name = COL_IMDB_VOTES) val imdbVotes: String? = null,
        @get:StorIOSQLiteColumn(name = COL_TOMATO_METER) val tomatoMeter: String? = null,
        @get:StorIOSQLiteColumn(name = COL_TOMATO_IMAGE) val tomatoImage: String? = null,
        @get:StorIOSQLiteColumn(name = COL_TOMATO_RATING) val tomatoRating: String? = null,
        @get:StorIOSQLiteColumn(name = COL_TOMATO_URL) val tomatoURL: String? = null,
        @get:StorIOSQLiteColumn(name = COL_TOMATO_USER_METER) val tomatoUserMeter: String? = null,
        @get:StorIOSQLiteColumn(name = COL_TOMATO_USER_RATING) val tomatoUserRating: String? = null,
        @get:StorIOSQLiteColumn(name = COL_PRODUCTION) val production: String? = null
)