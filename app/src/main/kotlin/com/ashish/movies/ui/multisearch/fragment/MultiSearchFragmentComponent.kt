package com.ashish.movies.ui.multisearch.fragment

import com.ashish.movies.di.modules.FragmentModule
import com.ashish.movies.di.multibindings.AbstractComponent
import com.ashish.movies.di.multibindings.fragment.FragmentComponentBuilder
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(FragmentModule::class))
interface MultiSearchFragmentComponent : AbstractComponent<MultiSearchFragment> {

    @Subcomponent.Builder
    interface Builder : FragmentComponentBuilder<MultiSearchFragment, MultiSearchFragmentComponent> {
        fun withModule(module: FragmentModule): Builder
    }
}