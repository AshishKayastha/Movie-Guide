package com.ashish.movies.di.components

import com.ashish.movies.di.modules.ApiModule
import com.ashish.movies.di.modules.AppModule
import com.ashish.movies.di.modules.NetModule
import com.ashish.movies.ui.movie.MovieModule
import com.ashish.movies.ui.movie.MovieSubComponent
import com.ashish.movies.ui.moviedetail.MovieDetailModule
import com.ashish.movies.ui.moviedetail.MovieDetailSubComponent
import com.ashish.movies.ui.people.PeopleModule
import com.ashish.movies.ui.people.PeopleSubComponent
import com.ashish.movies.ui.tvshow.TVShowModule
import com.ashish.movies.ui.tvshow.TVShowSubComponent
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Ashish on Dec 27.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, NetModule::class, ApiModule::class))
interface AppComponent {

    fun plus(movieModule: MovieModule): MovieSubComponent

    fun plus(tvShowModule: TVShowModule): TVShowSubComponent

    fun plus(peopleModule: PeopleModule): PeopleSubComponent

    fun plus(movieDetailModule: MovieDetailModule): MovieDetailSubComponent
}