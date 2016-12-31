package com.ashish.movies.ui.movie

import com.ashish.movies.data.interactors.TVShowInteractor
import com.ashish.movies.ui.tvshow.TVShowPresenter
import dagger.Module
import dagger.Provides

/**
 * Created by Ashish on Dec 30.
 */
@Module
class TVShowModule {

    @Provides
    fun provideTVShowPresenter(tvShowInteractor: TVShowInteractor): TVShowPresenter = TVShowPresenter(tvShowInteractor)
}