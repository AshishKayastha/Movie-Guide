package com.ashish.movieguide.data.database.tables

/**
 * Created by Ashish on Feb 05.
 */
object GenresTable : DatabaseTable {

    override val TABLE_NAME = "genres"

    const val COL_ID = "_id"
    const val COL_NAME = "name"
    const val COL_MEDIA_ID = "media_id"

    override val CREATE_TABLE = "CREATE TABLE $TABLE_NAME(" +
            "$COL_ID INTEGER NOT NULL PRIMARY KEY, " +
            "$COL_MEDIA_ID INTEGER NOT NULL, " +
            "$COL_NAME TEXT NOT NULL);"
}