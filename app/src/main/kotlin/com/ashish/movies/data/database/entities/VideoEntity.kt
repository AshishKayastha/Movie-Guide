package com.ashish.movies.data.database.entities

import com.ashish.movies.data.database.tables.VideosTable.COL_ID
import com.ashish.movies.data.database.tables.VideosTable.COL_KEY
import com.ashish.movies.data.database.tables.VideosTable.COL_MEDIA_ID
import com.ashish.movies.data.database.tables.VideosTable.COL_NAME
import com.ashish.movies.data.database.tables.VideosTable.COL_SITE
import com.ashish.movies.data.database.tables.VideosTable.COL_SIZE
import com.ashish.movies.data.database.tables.VideosTable.COL_TYPE
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteCreator
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType

/**
 * Created by Ashish on Feb 05.
 */
@StorIOSQLiteType(table = "videos")
data class VideoEntity @StorIOSQLiteCreator constructor(
        @get:StorIOSQLiteColumn(name = COL_ID, key = true) val id: String,
        @get:StorIOSQLiteColumn(name = COL_MEDIA_ID) val mediaId: Long,
        @get:StorIOSQLiteColumn(name = COL_NAME) val name: String? = null,
        @get:StorIOSQLiteColumn(name = COL_SITE) val site: String? = null,
        @get:StorIOSQLiteColumn(name = COL_SIZE) val size: Int? = null,
        @get:StorIOSQLiteColumn(name = COL_KEY) val key: String? = null,
        @get:StorIOSQLiteColumn(name = COL_TYPE) val type: String? = null
)