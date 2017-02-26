package com.ashish.movieguide.ui.discover.tvshow

import com.ashish.movieguide.di.multibindings.AbstractComponent
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilder
import com.ashish.movieguide.di.scopes.FragmentScope
import com.ashish.movieguide.ui.discover.filter.FilterFragmentBinder
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = arrayOf(FilterFragmentBinder::class))
interface DiscoverTVShowComponent : AbstractComponent<DiscoverTVShowFragment> {

    @Subcomponent.Builder
    interface Builder : FragmentComponentBuilder<DiscoverTVShowFragment, DiscoverTVShowComponent>
}