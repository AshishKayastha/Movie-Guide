package com.ashish.movieguide.ui.tvshow.detail

import android.content.Context
import dagger.Binds
import dagger.Module

@Module
@Suppress("unused")
abstract class TVShowDetailModule {

    @Binds
    abstract fun provideContext(activity: TVShowDetailActivity): Context
}