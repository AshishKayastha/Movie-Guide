package com.ashish.movies.ui.tvshow.detail

import com.ashish.movies.data.interactors.TVShowInteractor
import dagger.Module
import dagger.Provides

/**
 * Created by Ashish on Jan 03.
 */
@Module
class TVShowDetailModule {

    @Provides
    fun provideTVShowDetailPresenter(tvShowInteractor: TVShowInteractor) = TVShowDetailPresenter(tvShowInteractor)
}