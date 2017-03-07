package com.ashish.movieguide.ui.rated.movie

import com.ashish.movieguide.di.modules.FragmentModule
import com.ashish.movieguide.di.multibindings.AbstractComponent
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilder
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(FragmentModule::class))
interface RatedMovieComponent : AbstractComponent<RatedMovieFragment> {

    @Subcomponent.Builder
    interface Builder : FragmentComponentBuilder<RatedMovieFragment, RatedMovieComponent> {
        fun withModule(module: FragmentModule): Builder
    }
}