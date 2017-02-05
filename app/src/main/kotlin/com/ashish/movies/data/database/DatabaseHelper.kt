package com.ashish.movies.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ashish.movies.data.database.tables.ImagesTable
import com.ashish.movies.data.database.tables.MoviesTable
import com.ashish.movies.di.annotations.ForApplication
import javax.inject.Inject

/**
 * Created by Ashish on Feb 03.
 */
class DatabaseHelper @Inject constructor(@ForApplication context: Context)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "Movies.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        MoviesTable.onCreate(db)
        ImagesTable.onCreate(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        MoviesTable.dropTableIfExists(db)
        ImagesTable.dropTableIfExists(db)
    }
}