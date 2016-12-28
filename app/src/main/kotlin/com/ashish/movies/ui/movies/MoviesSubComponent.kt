package com.ashish.movies.ui.movies

import dagger.Subcomponent

/**
 * Created by Ashish on Dec 28.
 */
@Subcomponent(modules = arrayOf(MoviesModule::class))
interface MoviesSubComponent {

    fun inject(moviesFragment: MoviesFragment)
}