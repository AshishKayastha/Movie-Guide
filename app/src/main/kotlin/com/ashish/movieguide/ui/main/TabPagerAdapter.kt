package com.ashish.movieguide.ui.main

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.ashish.movieguide.ui.discover.movie.DiscoverMovieFragment
import com.ashish.movieguide.ui.discover.tvshow.DiscoverTVShowFragment
import com.ashish.movieguide.ui.movie.list.MovieFragment
import com.ashish.movieguide.ui.people.list.PeopleFragment
import com.ashish.movieguide.ui.tvshow.list.TVShowFragment

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
    }

    override fun getItem(position: Int) = when (contentType) {
        CONTENT_TYPE_MOVIE -> MovieFragment.newInstance(position)
        CONTENT_TYPE_TV_SHOW -> TVShowFragment.newInstance(position)
        CONTENT_TYPE_PEOPLE -> PeopleFragment.newInstance()
        CONTENT_TYPE_DISCOVER -> {
            if (position == 0) {
                DiscoverMovieFragment.newInstance()
            } else {
                DiscoverTVShowFragment.newInstance()
            }
        }
        else -> throw IllegalArgumentException("Invalid content type: $contentType")
    }

    override fun getCount() = tabTitles.size

    override fun getPageTitle(position: Int) = tabTitles[position]
}