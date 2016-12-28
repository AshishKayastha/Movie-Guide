package com.ashish.movies.ui.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.ashish.movies.ui.movies.MoviesFragment

/**
 * Created by Ashish on Dec 27.
 */
class TabPagerAdapter constructor(fragmentManager: FragmentManager, var tabTitles: Array<String>)
    : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment? {
        return MoviesFragment.newInstance(position)
    }

    fun updateTabTitles(tabTitles: Array<String>) {
        this.tabTitles = tabTitles
        notifyDataSetChanged()
    }

    override fun getCount() = tabTitles.size

    override fun getPageTitle(position: Int) = tabTitles[position]
}