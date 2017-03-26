package com.ashish.movieguide.di.modules

import android.app.Application
import android.content.Context
import com.ashish.movieguide.data.database.entities.Models
import com.ashish.movieguide.di.qualifiers.ApplicationContext
import com.ashish.movieguide.utils.extensions.defaultSharedPreferences
import dagger.Module
import dagger.Provides
import io.requery.Persistable
import io.requery.android.sqlite.DatabaseSource
import io.requery.reactivex.KotlinReactiveEntityStore
import io.requery.sql.KotlinEntityDataStore
import io.requery.sql.TableCreationMode
import javax.inject.Singleton

/**
 * Created by Ashish on Jan 02.
 */
@Module
class AppModule(private val application: Application) {

    companion object {
        private const val DATABASE_VERSION = 3
        private const val DATABASE_NAME = "MovieGuide.db"
    }

    @Provides
    @ApplicationContext
    fun provideAppContext(): Context = application

    @Provides
    @Singleton
    fun provideSharedPrefs(@ApplicationContext context: Context) = context.defaultSharedPreferences

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): KotlinReactiveEntityStore<Persistable> {
        val source = DatabaseSource(context, Models.DEFAULT, DATABASE_NAME, DATABASE_VERSION)
        source.setTableCreationMode(TableCreationMode.DROP_CREATE)
        source.setLoggingEnabled(true)
        return KotlinReactiveEntityStore(KotlinEntityDataStore(source.configuration))
    }
}