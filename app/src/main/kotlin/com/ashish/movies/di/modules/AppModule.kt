package com.ashish.movies.di.modules

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import com.ashish.movies.app.MoviesApp
import com.ashish.movies.data.database.DatabaseHelper
import com.ashish.movies.data.database.entities.CreditEntity
import com.ashish.movies.data.database.entities.CreditEntitySQLiteTypeMapping
import com.ashish.movies.data.database.entities.GenreEntity
import com.ashish.movies.data.database.entities.GenreEntitySQLiteTypeMapping
import com.ashish.movies.data.database.entities.ImageEntity
import com.ashish.movies.data.database.entities.ImageEntitySQLiteTypeMapping
import com.ashish.movies.data.database.entities.MovieDetailEntity
import com.ashish.movies.data.database.entities.MovieEntity
import com.ashish.movies.data.database.entities.VideoEntity
import com.ashish.movies.data.database.entities.VideoEntitySQLiteTypeMapping
import com.ashish.movies.data.database.resolvers.movie.MovieSQLiteTypeMapping
import com.ashish.movies.data.database.resolvers.moviedetail.MovieDetailSQLiteTypeMapping
import com.ashish.movies.di.annotations.ForApplication
import com.pushtorefresh.storio.sqlite.StorIOSQLite
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Ashish on Jan 02.
 */
@Module
class AppModule(val moviesApp: MoviesApp) {

    @Provides
    @Singleton
    @ForApplication
    fun provideAppContext(): Context = moviesApp

    @Provides
    @Singleton
    fun provideSQLiteOperHelper(@ForApplication context: Context): SQLiteOpenHelper = DatabaseHelper(context)

    @Provides
    @Singleton
    fun provideStorIOSQlite(sqLiteOpenHelper: SQLiteOpenHelper): StorIOSQLite {
        return DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(sqLiteOpenHelper)
                .addTypeMapping(MovieEntity::class.java, MovieSQLiteTypeMapping())
                .addTypeMapping(GenreEntity::class.java, GenreEntitySQLiteTypeMapping())
                .addTypeMapping(ImageEntity::class.java, ImageEntitySQLiteTypeMapping())
                .addTypeMapping(VideoEntity::class.java, VideoEntitySQLiteTypeMapping())
                .addTypeMapping(CreditEntity::class.java, CreditEntitySQLiteTypeMapping())
                .addTypeMapping(MovieDetailEntity::class.java, MovieDetailSQLiteTypeMapping())
                .build()
    }
}