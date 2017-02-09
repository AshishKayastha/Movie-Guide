package com.ashish.movies.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ashish.movies.data.database.tables.CreditsTable
import com.ashish.movies.data.database.tables.DatabaseTable
import com.ashish.movies.data.database.tables.GenresTable
import com.ashish.movies.data.database.tables.ImagesTable
import com.ashish.movies.data.database.tables.MoviesTable
import com.ashish.movies.data.database.tables.OMDbTable
import com.ashish.movies.data.database.tables.SimilarMoviesTable
import com.ashish.movies.data.database.tables.VideosTable
import com.ashish.movies.di.annotations.ApplicationQualifier
import javax.inject.Inject

/**
 * Created by Ashish on Feb 03.
 */
class DatabaseHelper @Inject constructor(@ApplicationQualifier context: Context)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "Movies.db"

        @JvmStatic
        private val TABLE_LIST = arrayOf(
                MoviesTable.javaClass,
                OMDbTable.javaClass,
                ImagesTable.javaClass,
                VideosTable.javaClass,
                GenresTable.javaClass,
                CreditsTable.javaClass,
                SimilarMoviesTable.javaClass
        )
    }

    override fun onCreate(db: SQLiteDatabase) = performDatabaseAction { it.onCreate(db) }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        performDatabaseAction { it.dropTableIfExists(db) }
        onCreate(db)
    }

    private fun performDatabaseAction(action: (DatabaseTable) -> Unit) {
        TABLE_LIST.map { it.interfaces[0] }
                .filterIsInstance<DatabaseTable>()
                .forEach { action(it) }
    }
}