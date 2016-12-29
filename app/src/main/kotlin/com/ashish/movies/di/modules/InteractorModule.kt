package com.ashish.movies.di.modules

import com.ashish.movies.data.api.MovieService
import com.ashish.movies.data.api.TVShowService
import com.ashish.movies.data.interactors.MovieInteractor
import com.ashish.movies.data.interactors.TVShowInteractor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Ashish on Dec 29.
 */
@Module
class InteractorModule {

    @Provides
    @Singleton
    fun provideMovieInteractor(movieService: MovieService): MovieInteractor = MovieInteractor(movieService)

    @Provides
    @Singleton
    fun provideTVShowInteractor(tvShowService: TVShowService): TVShowInteractor = TVShowInteractor(tvShowService)
}