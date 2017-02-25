package com.ashish.movies.di.modules

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides

/**
 * Created by Ashish on Feb 20.
 */
@Module
class ActivityModule(private val activity: Activity) {

    @Provides
    fun provideActivity() = activity

    @Provides
    fun provideContext(): Context = activity
}