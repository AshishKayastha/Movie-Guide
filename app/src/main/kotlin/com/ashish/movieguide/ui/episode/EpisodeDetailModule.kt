package com.ashish.movieguide.ui.episode

import android.content.Context
import com.ashish.movieguide.ui.base.detail.BaseDetailModule
import dagger.Binds
import dagger.Module

@Module
@Suppress("unused")
abstract class EpisodeDetailModule : BaseDetailModule<EpisodeDetailActivity>() {

    @Binds
    abstract fun provideContext(activity: EpisodeDetailActivity): Context
}