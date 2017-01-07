package com.ashish.movies.di.components

import com.ashish.movies.di.modules.ApiModule
import com.ashish.movies.di.modules.AppModule
import com.ashish.movies.di.modules.NetModule
import com.ashish.movies.ui.discover.common.DiscoverModule
import com.ashish.movies.ui.discover.common.DiscoverSubComponent
import com.ashish.movies.ui.movie.detail.MovieDetailModule
import com.ashish.movies.ui.movie.detail.MovieDetailSubComponent
import com.ashish.movies.ui.movie.list.MovieModule
import com.ashish.movies.ui.movie.list.MovieSubComponent
import com.ashish.movies.ui.multisearch.MultiSearchModule
import com.ashish.movies.ui.multisearch.MultiSearchSubComponent
import com.ashish.movies.ui.people.detail.PersonDetailModule
import com.ashish.movies.ui.people.detail.PersonDetailSubComponent
import com.ashish.movies.ui.people.list.PeopleModule
import com.ashish.movies.ui.people.list.PeopleSubComponent
import com.ashish.movies.ui.tvshow.detail.TVShowDetailModule
import com.ashish.movies.ui.tvshow.detail.TVShowDetailSubComponent
import com.ashish.movies.ui.tvshow.list.TVShowModule
import com.ashish.movies.ui.tvshow.list.TVShowSubComponent
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

    fun plus(tvShowDetailModule: TVShowDetailModule): TVShowDetailSubComponent

    fun plus(personDetailModule: PersonDetailModule): PersonDetailSubComponent

    fun plus(multiSearchModule: MultiSearchModule): MultiSearchSubComponent

    fun plus(discoverModule: DiscoverModule): DiscoverSubComponent
}