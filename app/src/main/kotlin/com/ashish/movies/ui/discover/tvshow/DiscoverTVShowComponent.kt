package com.ashish.movies.ui.discover.tvshow

import com.ashish.movies.di.multibindings.AbstractComponent
import com.ashish.movies.di.multibindings.fragment.FragmentComponentBuilder
import com.ashish.movies.di.scopes.FragmentScope
import com.ashish.movies.ui.discover.filter.FilterFragmentBinder
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = arrayOf(FilterFragmentBinder::class))
interface DiscoverTVShowComponent : AbstractComponent<DiscoverTVShowFragment> {

    @Subcomponent.Builder
    interface Builder : FragmentComponentBuilder<DiscoverTVShowFragment, DiscoverTVShowComponent>
}