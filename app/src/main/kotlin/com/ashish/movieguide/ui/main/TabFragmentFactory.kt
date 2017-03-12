package com.ashish.movieguide.ui.main

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
import com.ashish.movieguide.utils.Constants.FAVORITES
import com.ashish.movieguide.utils.Constants.WATCHLIST

object TabFragmentFactory {

    const val CONTENT_TYPE_MOVIE = 0
    const val CONTENT_TYPE_TV_SHOW = 1
    const val CONTENT_TYPE_PEOPLE = 2
    const val CONTENT_TYPE_DISCOVER = 3
    const val CONTENT_TYPE_FAVORITES = 4
    const val CONTENT_TYPE_WATCHLIST = 5
    const val CONTENT_TYPE_RATED = 6

    fun getFragment(contentType: Int, position: Int) = when (contentType) {
        CONTENT_TYPE_MOVIE -> MovieFragment.newInstance(position)

        CONTENT_TYPE_TV_SHOW -> TVShowFragment.newInstance(position)

        CONTENT_TYPE_PEOPLE -> PeopleFragment.newInstance()

        CONTENT_TYPE_DISCOVER -> getDiscoverFragment(position)

        CONTENT_TYPE_FAVORITES -> getPersonalFragment(position, FAVORITES)

        CONTENT_TYPE_WATCHLIST -> getPersonalFragment(position, WATCHLIST)

        CONTENT_TYPE_RATED -> getRatedFragment(position)

        else -> throw IllegalArgumentException("Invalid content type: $contentType")
    }

    private fun getDiscoverFragment(position: Int) = if (position == 0) {
        DiscoverMovieFragment.newInstance()
    } else {
        DiscoverTVShowFragment.newInstance()
    }

    private fun getPersonalFragment(position: Int, type: Int) = if (position == 0) {
        PersonalMovieFragment.newInstance(type)
    } else {
        PersonalTVShowFragment.newInstance(type)
    }

    private fun getRatedFragment(position: Int) = if (position == 0) {
        RatedMovieFragment.newInstance()
    } else if (position == 1) {
        RatedTVShowFragment.newInstance()
    } else {
        RatedEpisodeFragment.newInstance()
    }
}