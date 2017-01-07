package com.ashish.movies.ui.discover.common

import com.ashish.movies.data.interactors.MovieInteractor
import com.ashish.movies.data.interactors.TVShowInteractor
import com.ashish.movies.ui.discover.movie.DiscoverMoviePresenter
import com.ashish.movies.ui.discover.tvshow.DiscoverTVShowPresenter
import dagger.Module
import dagger.Provides

/**
 * Created by Ashish on Jan 06.
 */
@Module
class DiscoverModule {

    @Provides
    fun provideDiscoverMoviePresenter(movieInteractor: MovieInteractor) = DiscoverMoviePresenter(movieInteractor)

    @Provides
    fun provideDiscoverTVShowPresenter(tvShowInteractor: TVShowInteractor) = DiscoverTVShowPresenter(tvShowInteractor)
}