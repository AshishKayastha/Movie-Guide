package com.ashish.movies.ui.movies

import dagger.Module
import dagger.Provides

/**
 * Created by Ashish on Dec 27.
 */
@Module
class MoviesModule {

    @Provides
    fun provideMoviesPresenter(): MoviesPresenter {
        return MoviesPresenter()
    }
}