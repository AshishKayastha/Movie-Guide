package com.ashish.movieguide.ui.episode

import android.content.Context
import dagger.Binds
import dagger.Module

@Module
@Suppress("unused")
abstract class EpisodeDetailModule {

    @Binds
    abstract fun provideContext(activity: EpisodeDetailActivity): Context
}