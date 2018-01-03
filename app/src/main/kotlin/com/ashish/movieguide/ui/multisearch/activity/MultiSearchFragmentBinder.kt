package com.ashish.movieguide.ui.multisearch.activity

import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilder
import com.ashish.movieguide.di.multibindings.fragment.FragmentKey
import com.ashish.movieguide.ui.multisearch.fragment.MultiSearchFragment
import com.ashish.movieguide.ui.multisearch.fragment.MultiSearchFragmentComponent
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(subcomponents = [MultiSearchFragmentComponent::class])
abstract class MultiSearchFragmentBinder {

    @Binds
    @IntoMap
    @FragmentKey(MultiSearchFragment::class)
    abstract fun multiSearchFragmentComponentBuilder(
            builder: MultiSearchFragmentComponent.Builder): FragmentComponentBuilder<*, *>
}