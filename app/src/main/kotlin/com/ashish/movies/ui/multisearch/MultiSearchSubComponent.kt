package com.ashish.movies.ui.multisearch

import dagger.Subcomponent

/**
 * Created by Ashish on Jan 05.
 */
@Subcomponent(modules = arrayOf(MultiSearchModule::class))
interface MultiSearchSubComponent {

    fun inject(multiSearchActivity: MultiSearchActivity)

    fun inject(multiSearchFragment: MultiSearchFragment)
}