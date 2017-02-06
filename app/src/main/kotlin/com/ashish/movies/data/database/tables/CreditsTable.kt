package com.ashish.movies.data.database.tables

import android.database.sqlite.SQLiteDatabase

/**
 * Created by Ashish on Feb 06.
 */
object CreditsTable {

    const val CREDIT_TYE_CAST = "cast"
    const val CREDIT_TYE_CREW = "crew"

    const val TABLE_NAME = "credits"

    const val COL_ID = "_id"
    const val COL_MEDIA_ID = "media_id"
    const val COL_CREDIT_TYPE = "credit_type"
    const val COL_NAME = "name"
    const val COL_TITLE = "title"
    const val COL_JOB = "job"
    const val COL_CHARACTER = "character"
    const val COL_MEDIA_TYPE = "media_type"
    const val COL_POSTER_PATH = "poster_path"
    const val COL_RELEASE_DATE = "release_date"
    const val COL_PROFILE_PATH = "profile_path"

    private const val CREATE_TABLE = "CREATE TABLE $TABLE_NAME(" +
            "$COL_ID INTEGER NOT NULL PRIMARY KEY, " +
            "$COL_MEDIA_ID INTEGER NOT NULL, " +
            "$COL_CREDIT_TYPE TEXT NOT NULL, " +
            "$COL_NAME TEXT NOT NULL, " +
            "$COL_TITLE TEXT, " +
            "$COL_JOB TEXT, " +
            "$COL_CHARACTER TEXT, " +
            "$COL_MEDIA_TYPE TEXT, " +
            "$COL_POSTER_PATH TEXT, " +
            "$COL_RELEASE_DATE TEXT, " +
            "$COL_PROFILE_PATH TEXT);"

    fun onCreate(db: SQLiteDatabase) = db.execSQL(CREATE_TABLE)

    fun dropTableIfExists(db: SQLiteDatabase) = db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
}