package com.ashish.movieguide.ui.discover.filter

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
@Suppress("unused")
abstract class FilterModule {

    @ContributesAndroidInjector
    abstract fun contributeFilterFragment(): FilterBottomSheetDialogFragment
}