package com.ashish.movieguide.ui.people.list

import com.ashish.movieguide.di.modules.FragmentModule
import com.ashish.movieguide.di.multibindings.AbstractComponent
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilder
import dagger.Subcomponent

@Subcomponent(modules = [FragmentModule::class])
interface PeopleComponent : AbstractComponent<PeopleFragment> {

    @Subcomponent.Builder
    interface Builder : FragmentComponentBuilder<PeopleFragment, PeopleComponent> {
        fun withModule(module: FragmentModule): Builder
    }
}