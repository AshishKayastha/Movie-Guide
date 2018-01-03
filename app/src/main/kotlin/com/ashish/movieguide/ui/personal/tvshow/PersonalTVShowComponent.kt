package com.ashish.movieguide.ui.personal.tvshow

import com.ashish.movieguide.di.modules.FragmentModule
import com.ashish.movieguide.di.multibindings.AbstractComponent
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilder
import dagger.Subcomponent

@Subcomponent(modules = [FragmentModule::class])
interface PersonalTVShowComponent : AbstractComponent<PersonalTVShowFragment> {

    @Subcomponent.Builder
    interface Builder : FragmentComponentBuilder<PersonalTVShowFragment, PersonalTVShowComponent> {
        fun withModule(module: FragmentModule): Builder
    }
}