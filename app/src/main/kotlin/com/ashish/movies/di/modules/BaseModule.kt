package com.ashish.movies.di.modules

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides

/**
 * Created by Ashish on Jan 12.
 */
@Module
abstract class BaseModule(private val activity: Activity) {

    @Provides
    fun provideActivity() = activity

    @Provides
    fun provideContext(): Context = activity.baseContext
}