package com.ashish.movies.data.database.tables

import android.database.sqlite.SQLiteDatabase

/**
 * Created by Ashish on Feb 06.
 */
object SimilarMoviesTable {

    const val TABLE_NAME = "similar_movies"

    const val COL_ID = "_id"
    const val COL_MEDIA_ID = "media_id"
    const val COL_TITLE = "title"
    const val COL_OVERVIEW = "overview"
    const val COL_VOTE_COUNT = "vote_count"
    const val COL_POSTER_PATH = "poster_path"
    const val COL_RELEASE_DATE = "release_date"
    const val COL_VOTE_AVERAGE = "vote_average"
    const val COL_BACKDROP_PATH = "backdrop_path"

    private const val CREATE_TABLE = "CREATE TABLE $TABLE_NAME(" +
            "$COL_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            "$COL_MEDIA_ID INTEGER NOT NULL, " +
            "$COL_TITLE TEXT NOT NULL, " +
            "$COL_OVERVIEW TEXT, " +
            "$COL_RELEASE_DATE TEXT, " +
            "$COL_VOTE_COUNT INTEGER, " +
            "$COL_VOTE_AVERAGE INTEGER, " +
            "$COL_POSTER_PATH TEXT, " +
            "$COL_BACKDROP_PATH TEXT);"

    fun onCreate(db: SQLiteDatabase) = db.execSQL(CREATE_TABLE)

    fun dropTableIfExists(db: SQLiteDatabase) = db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
}