package com.ashish.movies.di.components

import com.ashish.movies.di.modules.UiModule
import com.ashish.movies.ui.discover.common.DiscoverComponent
import com.ashish.movies.ui.main.MainActivity
import com.ashish.movies.ui.movie.detail.MovieDetailActivity
import com.ashish.movies.ui.movie.list.MovieFragment
import com.ashish.movies.ui.multisearch.MultiSearchActivity
import com.ashish.movies.ui.multisearch.MultiSearchFragment
import com.ashish.movies.ui.people.detail.PersonDetailActivity
import com.ashish.movies.ui.people.list.PeopleFragment
import com.ashish.movies.ui.tvshow.detail.TVShowDetailActivity
import com.ashish.movies.ui.tvshow.episode.EpisodeDetailActivity
import com.ashish.movies.ui.tvshow.list.TVShowFragment
import com.ashish.movies.ui.tvshow.season.SeasonDetailActivity
import dagger.Subcomponent

/**
 * Created by Ashish on Feb 09.
 */
@Subcomponent(modules = arrayOf(UiModule::class))
interface UiComponent {

    fun inject(mainActivity: MainActivity)

    fun inject(movieFragment: MovieFragment)

    fun inject(tvShowFragment: TVShowFragment)

    fun inject(peopleFragment: PeopleFragment)

    fun inject(multiSearchActivity: MultiSearchActivity)

    fun inject(multiSearchFragment: MultiSearchFragment)

    fun inject(movieDetailActivity: MovieDetailActivity)

    fun inject(tvShowDetailActivity: TVShowDetailActivity)

    fun inject(seasonDetailActivity: SeasonDetailActivity)

    fun inject(episodeDetailActivity: EpisodeDetailActivity)

    fun inject(personDetailActivity: PersonDetailActivity)

    fun createDiscoverComponent(): DiscoverComponent
}