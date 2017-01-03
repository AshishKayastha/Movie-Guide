package com.ashish.movies.ui.movie.detail

import dagger.Subcomponent

/**
 * Created by Ashish on Dec 31.
 */
@Subcomponent(modules = arrayOf(MovieDetailModule::class))
interface MovieDetailSubComponent {

    fun inject(movieDetailActivity: MovieDetailActivity)
}