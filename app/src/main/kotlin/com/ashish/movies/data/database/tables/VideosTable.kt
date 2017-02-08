package com.ashish.movies.data.database.tables

/**
 * Created by Ashish on Feb 05.
 */
object VideosTable : DatabaseTable {

    override val TABLE_NAME = "videos"

    const val COL_ID = "_id"
    const val COL_NAME = "name"
    const val COL_SIZE = "size"
    const val COL_SITE = "site"
    const val COL_KEY = "key"
    const val COL_TYPE = "type"
    const val COL_MEDIA_ID = "media_id"

    override val CREATE_TABLE = "CREATE TABLE $TABLE_NAME(" +
            "$COL_ID INTEGER NOT NULL PRIMARY KEY, " +
            "$COL_MEDIA_ID INTEGER NOT NULL, " +
            "$COL_NAME TEXT, " +
            "$COL_SIZE TEXT, " +
            "$COL_SITE TEXT, " +
            "$COL_KEY TEXT, " +
            "$COL_TYPE TEXT);"
}