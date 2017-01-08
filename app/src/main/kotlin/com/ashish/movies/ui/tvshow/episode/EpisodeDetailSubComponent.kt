package com.ashish.movies.ui.tvshow.episode

import dagger.Subcomponent

/**
 * Created by Ashish on Jan 08.
 */
@Subcomponent(modules = arrayOf(EpisodeDetailModule::class))
interface EpisodeDetailSubComponent {

    fun inject(episodeDetailActivity: EpisodeDetailActivity)
}