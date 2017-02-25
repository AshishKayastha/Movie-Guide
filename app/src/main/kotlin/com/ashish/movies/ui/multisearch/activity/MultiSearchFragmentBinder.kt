package com.ashish.movies.ui.multisearch.activity

import com.ashish.movies.di.multibindings.fragment.FragmentComponentBuilder
import com.ashish.movies.di.multibindings.fragment.FragmentKey
import com.ashish.movies.ui.multisearch.fragment.MultiSearchFragment
import com.ashish.movies.ui.multisearch.fragment.MultiSearchFragmentComponent
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(MultiSearchFragmentComponent::class))
abstract class MultiSearchFragmentBinder {

    @Binds
    @IntoMap
    @FragmentKey(MultiSearchFragment::class)
    abstract fun multiSearchFragmentComponentBuilder(
            builder: MultiSearchFragmentComponent.Builder): FragmentComponentBuilder<*, *>
}