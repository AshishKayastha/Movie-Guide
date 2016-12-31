package com.ashish.movies.di.components

import com.ashish.movies.di.modules.ApiModule
import com.ashish.movies.di.modules.NetModule
import com.ashish.movies.ui.movie.MovieModule
import com.ashish.movies.ui.movie.MovieSubComponent
import com.ashish.movies.ui.movie.TVShowModule
import com.ashish.movies.ui.movie.TVShowSubComponent
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Ashish on Dec 27.
 */
@Singleton
@Component(modules = arrayOf(NetModule::class, ApiModule::class))
interface AppComponent {

    fun plus(movieModule: MovieModule): MovieSubComponent

    fun plus(tvShowModule: TVShowModule): TVShowSubComponent
}