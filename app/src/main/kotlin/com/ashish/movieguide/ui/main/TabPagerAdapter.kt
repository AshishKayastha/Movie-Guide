package com.ashish.movieguide.ui.main

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.ashish.movieguide.ui.discover.movie.DiscoverMovieFragment
import com.ashish.movieguide.ui.discover.tvshow.DiscoverTVShowFragment
import com.ashish.movieguide.ui.movie.list.MovieFragment
import com.ashish.movieguide.ui.people.list.PeopleFragment
import com.ashish.movieguide.ui.personal.movie.PersonalMovieFragment
import com.ashish.movieguide.ui.personal.tvshow.PersonalTVShowFragment
import com.ashish.movieguide.ui.tvshow.list.TVShowFragment
import com.ashish.movieguide.utils.Constants.FAVORITES
import com.ashish.movieguide.utils.Constants.WATCHLIST

/**
 * Created by Ashish on Dec 27.
 */
class TabPagerAdapter(
        private val contentType: Int,
        fragmentManager: FragmentManager,
        private val tabTitles: Array<String>
) : FragmentStatePagerAdapter(fragmentManager) {

    companion object {
        const val CONTENT_TYPE_MOVIE = 0
        const val CONTENT_TYPE_TV_SHOW = 1
        const val CONTENT_TYPE_PEOPLE = 2
        const val CONTENT_TYPE_DISCOVER = 3
        const val CONTENT_TYPE_FAVORITES = 4
        const val CONTENT_TYPE_WATCHLIST = 5
    }

    override fun getItem(position: Int) = when (contentType) {
        CONTENT_TYPE_MOVIE -> MovieFragment.newInstance(position)

        CONTENT_TYPE_TV_SHOW -> TVShowFragment.newInstance(position)

        CONTENT_TYPE_PEOPLE -> PeopleFragment.newInstance()

        CONTENT_TYPE_DISCOVER -> if (position == 0) {
            DiscoverMovieFragment.newInstance()
        } else {
            DiscoverTVShowFragment.newInstance()
        }

        CONTENT_TYPE_FAVORITES -> getPersonalFragment(position, FAVORITES)

        CONTENT_TYPE_WATCHLIST -> getPersonalFragment(position, WATCHLIST)

        else -> throw IllegalArgumentException("Invalid content type: $contentType")
    }

    private fun getPersonalFragment(position: Int, type: Int) = if (position == 0) {
        PersonalMovieFragment.newInstance(type)
    } else {
        PersonalTVShowFragment.newInstance(type)
    }

    override fun getCount() = tabTitles.size

    override fun getPageTitle(position: Int) = tabTitles[position]
}