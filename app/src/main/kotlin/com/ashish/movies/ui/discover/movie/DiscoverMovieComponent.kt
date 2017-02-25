package com.ashish.movies.ui.discover.movie

import com.ashish.movies.di.multibindings.AbstractComponent
import com.ashish.movies.di.multibindings.fragment.FragmentComponentBuilder
import com.ashish.movies.di.scopes.FragmentScope
import com.ashish.movies.ui.discover.filter.FilterFragmentBinder
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = arrayOf(FilterFragmentBinder::class))
interface DiscoverMovieComponent : AbstractComponent<DiscoverMovieFragment> {

    @Subcomponent.Builder
    interface Builder : FragmentComponentBuilder<DiscoverMovieFragment, DiscoverMovieComponent>
}