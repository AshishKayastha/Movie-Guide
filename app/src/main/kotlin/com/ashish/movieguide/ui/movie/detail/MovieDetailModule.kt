package com.ashish.movieguide.ui.movie.detail

import android.content.Context
import com.ashish.movieguide.ui.base.detail.BaseDetailModule
import dagger.Binds
import dagger.Module

@Module
@Suppress("unused")
abstract class MovieDetailModule : BaseDetailModule<MovieDetailActivity>() {

    @Binds
    abstract fun provideContext(activity: MovieDetailActivity): Context
}