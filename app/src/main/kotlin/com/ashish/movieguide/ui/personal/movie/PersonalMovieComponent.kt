package com.ashish.movieguide.ui.personal.movie

import com.ashish.movieguide.di.modules.FragmentModule
import com.ashish.movieguide.di.multibindings.AbstractComponent
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilder
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(FragmentModule::class))
interface PersonalMovieComponent : AbstractComponent<PersonalMovieFragment> {

    @Subcomponent.Builder
    interface Builder : FragmentComponentBuilder<PersonalMovieFragment, PersonalMovieComponent> {
        fun withModule(module: FragmentModule): Builder
    }
}