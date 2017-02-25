package com.ashish.movies.ui.people.list

import com.ashish.movies.di.modules.FragmentModule
import com.ashish.movies.di.multibindings.AbstractComponent
import com.ashish.movies.di.multibindings.fragment.FragmentComponentBuilder
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(FragmentModule::class))
interface PeopleComponent : AbstractComponent<PeopleFragment> {

    @Subcomponent.Builder
    interface Builder : FragmentComponentBuilder<PeopleFragment, PeopleComponent> {
        fun withModule(module: FragmentModule): Builder
    }
}