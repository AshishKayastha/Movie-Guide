package com.ashish.movies.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ashish.movies.data.database.tables.CreditsTable
import com.ashish.movies.data.database.tables.GenresTable
import com.ashish.movies.data.database.tables.ImagesTable
import com.ashish.movies.data.database.tables.MoviesTable
import com.ashish.movies.data.database.tables.SimilarMoviesTable
import com.ashish.movies.data.database.tables.VideosTable
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
        VideosTable.onCreate(db)
        GenresTable.onCreate(db)
        CreditsTable.onCreate(db)
        SimilarMoviesTable.onCreate(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        MoviesTable.dropTableIfExists(db)
        ImagesTable.dropTableIfExists(db)
        VideosTable.dropTableIfExists(db)
        GenresTable.dropTableIfExists(db)
        CreditsTable.dropTableIfExists(db)
        SimilarMoviesTable.dropTableIfExists(db)
    }
}