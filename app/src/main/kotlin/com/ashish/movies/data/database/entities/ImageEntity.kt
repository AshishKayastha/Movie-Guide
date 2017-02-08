package com.ashish.movies.data.database.entities

import com.ashish.movies.data.database.tables.ImagesTable.COL_FILE_PATH
import com.ashish.movies.data.database.tables.ImagesTable.COL_ID
import com.ashish.movies.data.database.tables.ImagesTable.COL_MEDIA_ID
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteCreator
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType

/**
 * Created by Ashish on Feb 05.
 */
@StorIOSQLiteType(table = "images")
data class ImageEntity @StorIOSQLiteCreator constructor(
        @get:StorIOSQLiteColumn(name = COL_ID, key = true) val id: Long,
        @get:StorIOSQLiteColumn(name = COL_MEDIA_ID) val mediaId: Long,
        @get:StorIOSQLiteColumn(name = COL_FILE_PATH) val filePath: String
)