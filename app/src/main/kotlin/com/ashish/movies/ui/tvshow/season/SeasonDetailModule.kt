package com.ashish.movies.ui.tvshow.season

import com.ashish.movies.data.interactors.TVShowInteractor
import dagger.Module
import dagger.Provides

/**
 * Created by Ashish on Jan 07.
 */
@Module
class SeasonDetailModule {

    @Provides
    fun provideSeasonDetailPresenter(tvShowInteractor: TVShowInteractor) = SeasonDetailPresenter(tvShowInteractor)
}