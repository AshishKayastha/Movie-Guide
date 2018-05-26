package com.ashish.movieguide.ui.movie.detail

import android.content.Context
import dagger.Binds
import dagger.Module

@Module
@Suppress("unused")
abstract class MovieDetailModule {

    @Binds
    abstract fun provideContext(activity: MovieDetailActivity): Context
}