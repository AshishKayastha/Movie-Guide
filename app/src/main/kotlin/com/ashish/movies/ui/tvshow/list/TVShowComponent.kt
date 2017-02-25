package com.ashish.movies.ui.tvshow.list

import com.ashish.movies.di.modules.FragmentModule
import com.ashish.movies.di.multibindings.AbstractComponent
import com.ashish.movies.di.multibindings.fragment.FragmentComponentBuilder
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(FragmentModule::class))
interface TVShowComponent : AbstractComponent<TVShowFragment> {

    @Subcomponent.Builder
    interface Builder : FragmentComponentBuilder<TVShowFragment, TVShowComponent> {
        fun withModule(module: FragmentModule): Builder
    }
}