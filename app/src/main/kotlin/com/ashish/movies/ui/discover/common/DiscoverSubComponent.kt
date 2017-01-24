package com.ashish.movies.ui.discover.common

import com.ashish.movies.di.annotations.FragmentScope
import com.ashish.movies.ui.discover.common.filter.FilterSubComponent
import com.ashish.movies.ui.discover.movie.DiscoverMovieFragment
import com.ashish.movies.ui.discover.tvshow.DiscoverTVShowFragment
import dagger.Subcomponent

/**
 * Created by Ashish on Jan 06.
 */
@FragmentScope
@Subcomponent(modules = arrayOf(DiscoverModule::class))
interface DiscoverSubComponent {

    fun inject(discoverMovieFragment: DiscoverMovieFragment)

    fun inject(discoverTVShowFragment: DiscoverTVShowFragment)

    fun createFilterComponent(): FilterSubComponent
}