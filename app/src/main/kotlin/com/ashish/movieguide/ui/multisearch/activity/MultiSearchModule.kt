package com.ashish.movieguide.ui.multisearch.activity

import android.app.Activity
import com.ashish.movieguide.di.scopes.FragmentScope
import com.ashish.movieguide.ui.multisearch.fragment.MultiSearchFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
@Suppress("unused")
abstract class MultiSearchModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeMultiSearchFragment(): MultiSearchFragment

    @Binds
    abstract fun provideActivity(activity: MultiSearchActivity): Activity
}