package com.ashish.movies.ui.tvshow.episode

import com.ashish.movies.data.interactors.TVShowInteractor
import dagger.Module
import dagger.Provides

/**
 * Created by Ashish on Jan 07.
 */
@Module
class EpisodeDetailModule {

    @Provides
    fun provideEpisodeDetailPresenter(tvShowInteractor: TVShowInteractor) = EpisodeDetailPresenter(tvShowInteractor)
}