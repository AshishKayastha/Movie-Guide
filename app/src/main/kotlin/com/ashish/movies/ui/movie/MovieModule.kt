package com.ashish.movies.ui.movie

import com.ashish.movies.data.interactors.MovieInteractor
import dagger.Module
import dagger.Provides

/**
 * Created by Ashish on Dec 27.
 */
@Module
class MovieModule {

    @Provides
    fun provideMoviesPresenter(movieInteractor: MovieInteractor): MoviePresenter {
        return MoviePresenter(movieInteractor)
    }
}