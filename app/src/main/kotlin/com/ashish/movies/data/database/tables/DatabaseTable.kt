package com.ashish.movies.data.database.tables

import android.database.sqlite.SQLiteDatabase

/**
 * Created by Ashish on Feb 08.
 */
interface DatabaseTable {

    val TABLE_NAME: String
    val CREATE_TABLE: String

    fun onCreate(db: SQLiteDatabase) = db.execSQL(CREATE_TABLE)

    fun dropTableIfExists(db: SQLiteDatabase) = db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
}