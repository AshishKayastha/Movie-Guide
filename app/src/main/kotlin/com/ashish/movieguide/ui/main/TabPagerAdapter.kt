package com.ashish.movieguide.ui.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

/**
 * Created by Ashish on Dec 27.
 */
class TabPagerAdapter(
        private val contentType: Int,
        fragmentManager: FragmentManager,
        private val tabTitles: Array<String>
) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment = TabFragmentFactory.getFragment(contentType, position)

    override fun getCount() = tabTitles.size

    override fun getPageTitle(position: Int): CharSequence? = tabTitles[position]
}