package com.ashish.movies.ui.main

import dagger.Subcomponent

/**
 * Created by Ashish on Jan 28.
 */
@Subcomponent(modules = arrayOf(MainModule::class))
interface MainSubComponent {

    fun inject(mainActivity: MainActivity)
}