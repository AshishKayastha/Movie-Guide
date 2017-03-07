package com.ashish.movieguide.ui.rated.episode

import com.ashish.movieguide.di.modules.FragmentModule
import com.ashish.movieguide.di.multibindings.AbstractComponent
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilder
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(FragmentModule::class))
interface RatedEpisodeComponent : AbstractComponent<RatedEpisodeFragment> {

    @Subcomponent.Builder
    interface Builder : FragmentComponentBuilder<RatedEpisodeFragment, RatedEpisodeComponent> {
        fun withModule(module: FragmentModule): Builder
    }
}