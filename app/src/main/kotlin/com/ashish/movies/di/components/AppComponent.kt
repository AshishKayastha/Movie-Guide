package com.ashish.movies.di.components

import com.ashish.movies.di.modules.ApiModule
import com.ashish.movies.di.modules.InteractorModule
import com.ashish.movies.di.modules.NetModule
import com.ashish.movies.ui.movies.MoviesModule
import com.ashish.movies.ui.movies.MoviesSubComponent
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Ashish on Dec 27.
 */
@Singleton
@Component(modules = arrayOf(NetModule::class, ApiModule::class, InteractorModule::class))
interface AppComponent {

    fun plus(moviesModule: MoviesModule): MoviesSubComponent
}