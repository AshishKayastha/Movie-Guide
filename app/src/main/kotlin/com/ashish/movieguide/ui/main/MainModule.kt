package com.ashish.movieguide.ui.main

import com.ashish.movieguide.di.scopes.FragmentScope
import com.ashish.movieguide.ui.discover.filter.FilterModule
import com.ashish.movieguide.ui.discover.movie.DiscoverMovieFragment
import com.ashish.movieguide.ui.discover.tvshow.DiscoverTVShowFragment
import com.ashish.movieguide.ui.movie.list.MovieFragment
import com.ashish.movieguide.ui.people.list.PeopleFragment
import com.ashish.movieguide.ui.personal.movie.PersonalMovieFragment
import com.ashish.movieguide.ui.personal.tvshow.PersonalTVShowFragment
import com.ashish.movieguide.ui.rated.episode.RatedEpisodeFragment
import com.ashish.movieguide.ui.rated.movie.RatedMovieFragment
import com.ashish.movieguide.ui.rated.tvshow.RatedTVShowFragment
import com.ashish.movieguide.ui.tvshow.list.TVShowFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
@Suppress("unused")
abstract class MainModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeMovieFragment(): MovieFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeTVShowFragment(): TVShowFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributePeopleFragment(): PeopleFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [FilterModule::class])
    abstract fun contributeDiscoverMovieFragment(): DiscoverMovieFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [FilterModule::class])
    abstract fun contributeDiscoverTVShowFragment(): DiscoverTVShowFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributePersonalMovieFragment(): PersonalMovieFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributePersonalTVShowFragment(): PersonalTVShowFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeRatedMovieFragment(): RatedMovieFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeRatedTVShowFragment(): RatedTVShowFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeRatedEpisodeFragment(): RatedEpisodeFragment
}