package com.ashish.movieguide.ui.tvshow.detail

import android.content.Context
import com.ashish.movieguide.ui.base.detail.BaseDetailModule
import dagger.Binds
import dagger.Module

@Module
@Suppress("unused")
abstract class TVShowDetailModule : BaseDetailModule<TVShowDetailActivity>() {

    @Binds
    abstract fun provideContext(activity: TVShowDetailActivity): Context
}