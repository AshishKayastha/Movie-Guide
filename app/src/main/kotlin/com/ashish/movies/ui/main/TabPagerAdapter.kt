package com.ashish.movies.ui.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.ashish.movies.ui.movies.MoviesFragment

/**
 * Created by Ashish on Dec 27.
 */
class TabPagerAdapter constructor(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment? {
        return MoviesFragment.newInstance(position)
    }

    override fun getCount() = 2
}