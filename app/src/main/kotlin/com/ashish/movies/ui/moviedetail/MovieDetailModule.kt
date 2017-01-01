package com.ashish.movies.ui.moviedetail

import com.ashish.movies.data.interactors.MovieInteractor
import dagger.Module
import dagger.Provides

/**
 * Created by Ashish on Dec 31.
 */
@Module
class MovieDetailModule {

    @Provides
    fun provideMovieDetailPresenter(movieInteractor: MovieInteractor) = MovieDetailPresenter(movieInteractor)
}