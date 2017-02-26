package com.ashish.movieguide.ui.movie.list

import com.ashish.movieguide.di.modules.FragmentModule
import com.ashish.movieguide.di.multibindings.AbstractComponent
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilder
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(FragmentModule::class))
interface MovieComponent : AbstractComponent<MovieFragment> {

    @Subcomponent.Builder
    interface Builder : FragmentComponentBuilder<MovieFragment, MovieComponent> {
        fun withModule(module: FragmentModule): Builder
    }
}