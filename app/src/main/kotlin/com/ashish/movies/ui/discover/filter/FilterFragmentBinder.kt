package com.ashish.movies.ui.discover.filter

import com.ashish.movies.di.multibindings.fragment.FragmentComponentBuilder
import com.ashish.movies.di.multibindings.fragment.FragmentKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(FilterComponent::class))
abstract class FilterFragmentBinder {

    @Binds
    @IntoMap
    @FragmentKey(FilterBottomSheetDialogFragment::class)
    abstract fun filterComponentBuilder(builder: FilterComponent.Builder): FragmentComponentBuilder<*, *>
}