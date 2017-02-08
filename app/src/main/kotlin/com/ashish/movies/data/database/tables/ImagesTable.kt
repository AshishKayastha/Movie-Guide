package com.ashish.movies.data.database.tables

/**
 * Created by Ashish on Feb 05.
 */
object ImagesTable : DatabaseTable {

    override val TABLE_NAME = "images"

    const val COL_ID = "_id"
    const val COL_MEDIA_ID = "media_id"
    const val COL_FILE_PATH = "file_path"

    override val CREATE_TABLE = "CREATE TABLE $TABLE_NAME(" +
            "$COL_ID INTEGER NOT NULL PRIMARY KEY, " +
            "$COL_MEDIA_ID INTEGER NOT NULL, " +
            "$COL_FILE_PATH TEXT NOT NULL);"
}