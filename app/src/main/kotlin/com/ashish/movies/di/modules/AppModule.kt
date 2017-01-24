package com.ashish.movies.di.modules

import android.content.Context
import com.ashish.movies.app.MoviesApp
import com.ashish.movies.di.annotations.ForApplication
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
}