package com.ashish.movies.ui.movies

import com.ashish.movies.data.interactors.MovieInteractor
import dagger.Module
import dagger.Provides

/**
 * Created by Ashish on Dec 27.
 */
@Module
class MoviesModule {

    @Provides
    fun provideMoviesPresenter(movieInteractor: MovieInteractor): MoviesPresenter {
        return MoviesPresenter(movieInteractor)
    }
}