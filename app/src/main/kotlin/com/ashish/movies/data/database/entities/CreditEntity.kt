package com.ashish.movies.data.database.entities

import com.ashish.movies.data.database.tables.CreditsTable.COL_CHARACTER
import com.ashish.movies.data.database.tables.CreditsTable.COL_CREDIT_TYPE
import com.ashish.movies.data.database.tables.CreditsTable.COL_ID
import com.ashish.movies.data.database.tables.CreditsTable.COL_JOB
import com.ashish.movies.data.database.tables.CreditsTable.COL_MEDIA_ID
import com.ashish.movies.data.database.tables.CreditsTable.COL_MEDIA_TYPE
import com.ashish.movies.data.database.tables.CreditsTable.COL_NAME
import com.ashish.movies.data.database.tables.CreditsTable.COL_POSTER_PATH
import com.ashish.movies.data.database.tables.CreditsTable.COL_PROFILE_PATH
import com.ashish.movies.data.database.tables.CreditsTable.COL_RELEASE_DATE
import com.ashish.movies.data.database.tables.CreditsTable.COL_TITLE
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteCreator
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType

/**
 * Created by Ashish on Feb 06.
 */
@StorIOSQLiteType(table = "credits")
data class CreditEntity @StorIOSQLiteCreator constructor(
        @get:StorIOSQLiteColumn(name = COL_ID, key = true) val id: Long,
        @get:StorIOSQLiteColumn(name = COL_MEDIA_ID) val mediaId: Long,
        @get:StorIOSQLiteColumn(name = COL_CREDIT_TYPE) val creditType: String,
        @get:StorIOSQLiteColumn(name = COL_NAME) val name: String,
        @get:StorIOSQLiteColumn(name = COL_TITLE) val title: String? = null,
        @get:StorIOSQLiteColumn(name = COL_JOB) val job: String? = null,
        @get:StorIOSQLiteColumn(name = COL_CHARACTER) val character: String? = null,
        @get:StorIOSQLiteColumn(name = COL_MEDIA_TYPE) val mediaType: String? = null,
        @get:StorIOSQLiteColumn(name = COL_POSTER_PATH) val posterPath: String? = null,
        @get:StorIOSQLiteColumn(name = COL_RELEASE_DATE) val releaseDate: String? = null,
        @get:StorIOSQLiteColumn(name = COL_PROFILE_PATH) val profilePath: String? = null
)