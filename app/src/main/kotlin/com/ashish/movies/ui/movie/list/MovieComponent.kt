package com.ashish.movies.ui.movie.list

import com.ashish.movies.di.modules.FragmentModule
import com.ashish.movies.di.multibindings.AbstractComponent
import com.ashish.movies.di.multibindings.fragment.FragmentComponentBuilder
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(FragmentModule::class))
interface MovieComponent : AbstractComponent<MovieFragment> {

    @Subcomponent.Builder
    interface Builder : FragmentComponentBuilder<MovieFragment, MovieComponent> {
        fun withModule(module: FragmentModule): Builder
    }
}