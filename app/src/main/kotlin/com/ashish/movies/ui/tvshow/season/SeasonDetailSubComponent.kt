package com.ashish.movies.ui.tvshow.season

import dagger.Subcomponent

/**
 * Created by Ashish on Jan 07.
 */
@Subcomponent(modules = arrayOf(SeasonDetailModule::class))
interface SeasonDetailSubComponent {

    fun inject(seasonDetailActivity: SeasonDetailActivity)
}