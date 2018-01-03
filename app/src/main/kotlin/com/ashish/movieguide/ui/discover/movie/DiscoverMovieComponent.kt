package com.ashish.movieguide.ui.discover.movie

import com.ashish.movieguide.di.multibindings.AbstractComponent
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilder
import com.ashish.movieguide.di.scopes.FragmentScope
import com.ashish.movieguide.ui.discover.filter.FilterFragmentBinder
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [FilterFragmentBinder::class])
interface DiscoverMovieComponent : AbstractComponent<DiscoverMovieFragment> {

    @Subcomponent.Builder
    interface Builder : FragmentComponentBuilder<DiscoverMovieFragment, DiscoverMovieComponent>
}