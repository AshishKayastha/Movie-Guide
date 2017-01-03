package com.ashish.movies.ui.tvshow.detail

import dagger.Subcomponent

/**
 * Created by Ashish on Jan 03.
 */
@Subcomponent(modules = arrayOf(TVShowDetailModule::class))
interface TVShowDetailSubComponent {

    fun inject(tvShowDetailActivity: TVShowDetailActivity)
}