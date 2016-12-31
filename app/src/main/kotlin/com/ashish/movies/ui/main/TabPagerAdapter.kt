package com.ashish.movies.ui.main

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.ashish.movies.ui.movie.MovieFragment
import com.ashish.movies.ui.tvshow.TVShowFragment

/**
 * Created by Ashish on Dec 27.
 */
class TabPagerAdapter constructor(var contentType: Int, fragmentManager: FragmentManager, var tabTitles: Array<String>)
    : FragmentStatePagerAdapter(fragmentManager) {

    companion object {
        const val CONTENT_TYPE_MOVIE = 0
        const val CONTENT_TYPE_TV_SHOW = 1
        const val CONTENT_TYPE_PEOPLE = 2
    }

    override fun getItem(position: Int) = when (contentType) {
        CONTENT_TYPE_TV_SHOW -> TVShowFragment.newInstance(position)
        CONTENT_TYPE_PEOPLE -> TVShowFragment.newInstance(position)
        else -> MovieFragment.newInstance(position)
    }

    fun updateTabTitles(contentType: Int, tabTitles: Array<String>) {
        this.contentType = contentType
        this.tabTitles = tabTitles
        notifyDataSetChanged()
    }

    override fun getCount() = tabTitles.size

    override fun getPageTitle(position: Int) = tabTitles[position]
}