package com.ashish.movies.di.modules

import android.content.Context
import com.ashish.movies.app.MoviesApp
import com.ashish.movies.di.annotations.PerApplication
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
    @PerApplication
    fun provideAppContext(): Context = moviesApp
}