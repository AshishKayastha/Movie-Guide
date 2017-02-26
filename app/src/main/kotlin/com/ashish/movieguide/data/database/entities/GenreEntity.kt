package com.ashish.movieguide.data.database.entities

import com.ashish.movieguide.data.database.tables.GenresTable.COL_ID
import com.ashish.movieguide.data.database.tables.GenresTable.COL_MEDIA_ID
import com.ashish.movieguide.data.database.tables.GenresTable.COL_NAME
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteCreator
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType

/**
 * Created by Ashish on Feb 05.
 */
@StorIOSQLiteType(table = "genres")
data class GenreEntity @StorIOSQLiteCreator constructor(
        @get:StorIOSQLiteColumn(name = COL_ID, key = true) val id: Long,
        @get:StorIOSQLiteColumn(name = COL_MEDIA_ID) val mediaId: Long,
        @get:StorIOSQLiteColumn(name = COL_NAME) val name: String? = null
)