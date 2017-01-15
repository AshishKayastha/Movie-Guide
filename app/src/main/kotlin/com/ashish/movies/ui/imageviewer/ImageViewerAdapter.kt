package com.ashish.movies.ui.imageviewer

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.SparseArray
import android.view.ViewGroup
import java.util.*

/**
 * Created by Ashish on Jan 08.
 */
class ImageViewerAdapter(fm: FragmentManager, val imageUrlList: ArrayList<String>, val startingPosition: Int)
    : FragmentStatePagerAdapter(fm) {

    private var registeredFragments = SparseArray<ImageViewerFragment>()

    override fun getItem(position: Int): Fragment {
        return ImageViewerFragment.newInstance(position, startingPosition, imageUrlList[position])
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as ImageViewerFragment
        registeredFragments.put(position, fragment)
        return fragment
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any) {
        registeredFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    fun getRegisteredFragment(position: Int): ImageViewerFragment? {
        return registeredFragments.get(position)
    }

    override fun getCount() = imageUrlList.size
}