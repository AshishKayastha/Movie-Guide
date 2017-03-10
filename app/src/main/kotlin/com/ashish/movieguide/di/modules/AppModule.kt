package com.ashish.movieguide.di.modules

import android.app.Application
import android.content.Context
import com.ashish.movieguide.di.qualifiers.ApplicationContext
import com.ashish.movieguide.utils.extensions.defaultSharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Ashish on Jan 02.
 */
@Module
class AppModule(private val application: Application) {

    @Provides
    @ApplicationContext
    fun provideAppContext(): Context = application

    @Provides
    @Singleton
    fun provideSharedPrefs(@ApplicationContext context: Context) = context.defaultSharedPreferences
}