package com.ashish.movies.ui.movie.list

import com.ashish.movies.data.interactors.MovieInteractor
import dagger.Module
import dagger.Provides

/**
 * Created by Ashish on Dec 27.
 */
@Module
class MovieModule {

    @Provides
    fun provideMoviePresenter(movieInteractor: MovieInteractor) = MoviePresenter(movieInteractor)
}