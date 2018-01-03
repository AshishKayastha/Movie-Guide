package com.ashish.movieguide.ui.discover.filter

import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilder
import com.ashish.movieguide.di.multibindings.fragment.FragmentKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(subcomponents = [FilterComponent::class])
abstract class FilterFragmentBinder {

    @Binds
    @IntoMap
    @FragmentKey(FilterBottomSheetDialogFragment::class)
    abstract fun filterComponentBuilder(builder: FilterComponent.Builder): FragmentComponentBuilder<*, *>
}