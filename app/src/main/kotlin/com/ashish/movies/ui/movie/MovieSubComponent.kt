package com.ashish.movies.ui.movie

import dagger.Subcomponent

/**
 * Created by Ashish on Dec 28.
 */
@Subcomponent(modules = arrayOf(MovieModule::class))
interface MovieSubComponent {

    fun inject(movieFragment: MovieFragment)
}