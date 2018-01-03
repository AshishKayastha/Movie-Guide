package com.ashish.movieguide.ui.multisearch.fragment

import com.ashish.movieguide.di.modules.FragmentModule
import com.ashish.movieguide.di.multibindings.AbstractComponent
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilder
import dagger.Subcomponent

@Subcomponent(modules = [FragmentModule::class])
interface MultiSearchFragmentComponent : AbstractComponent<MultiSearchFragment> {

    @Subcomponent.Builder
    interface Builder : FragmentComponentBuilder<MultiSearchFragment, MultiSearchFragmentComponent> {
        fun withModule(module: FragmentModule): Builder
    }
}