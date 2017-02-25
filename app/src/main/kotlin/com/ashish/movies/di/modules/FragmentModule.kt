package com.ashish.movies.di.modules

import android.app.Activity
import android.content.Context
import com.ashish.movies.di.qualifiers.ForFragment
import dagger.Module
import dagger.Provides

/**
 * Created by Ashish on Feb 25.
 */
@Module
class FragmentModule(private val activity: Activity) {

    @Provides
    @ForFragment
    fun provideActivity() = activity

    @Provides
    @ForFragment
    fun provideContext(): Context = activity
}