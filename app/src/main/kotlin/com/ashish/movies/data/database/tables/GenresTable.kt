package com.ashish.movies.data.database.tables

import android.database.sqlite.SQLiteDatabase

/**
 * Created by Ashish on Feb 05.
 */
object GenresTable {

    const val TABLE_NAME = "genres"

    const val COL_ID = "_id"
    const val COL_NAME = "name"
    const val COL_MEDIA_ID = "media_id"

    private const val CREATE_TABLE = "CREATE TABLE $TABLE_NAME(" +
            "$COL_ID INTEGER NOT NULL PRIMARY KEY, " +
            "$COL_MEDIA_ID INTEGER NOT NULL, " +
            "$COL_NAME TEXT NOT NULL);"

    fun onCreate(db: SQLiteDatabase) = db.execSQL(CREATE_TABLE)

    fun dropTableIfExists(db: SQLiteDatabase) = db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
}