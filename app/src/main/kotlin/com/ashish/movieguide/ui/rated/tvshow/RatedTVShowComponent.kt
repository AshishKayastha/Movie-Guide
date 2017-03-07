package com.ashish.movieguide.ui.rated.tvshow

import com.ashish.movieguide.di.modules.FragmentModule
import com.ashish.movieguide.di.multibindings.AbstractComponent
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilder
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(FragmentModule::class))
interface RatedTVShowComponent : AbstractComponent<RatedTVShowFragment> {

    @Subcomponent.Builder
    interface Builder : FragmentComponentBuilder<RatedTVShowFragment, RatedTVShowComponent> {
        fun withModule(module: FragmentModule): Builder
    }
}