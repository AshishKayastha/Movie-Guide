package com.ashish.movies.ui.tvshow

import dagger.Subcomponent

/**
 * Created by Ashish on Dec 30.
 */
@Subcomponent(modules = arrayOf(TVShowModule::class))
interface TVShowSubComponent {

    fun inject(tvShowFragment: TVShowFragment)
}