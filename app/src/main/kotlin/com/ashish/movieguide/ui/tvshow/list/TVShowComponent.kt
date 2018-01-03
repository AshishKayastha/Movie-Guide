package com.ashish.movieguide.ui.tvshow.list

import com.ashish.movieguide.di.modules.FragmentModule
import com.ashish.movieguide.di.multibindings.AbstractComponent
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilder
import dagger.Subcomponent

@Subcomponent(modules = [FragmentModule::class])
interface TVShowComponent : AbstractComponent<TVShowFragment> {

    @Subcomponent.Builder
    interface Builder : FragmentComponentBuilder<TVShowFragment, TVShowComponent> {
        fun withModule(module: FragmentModule): Builder
    }
}