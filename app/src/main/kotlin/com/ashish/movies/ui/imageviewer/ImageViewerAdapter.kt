package com.ashish.movies.ui.imageviewer

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import java.util.*

/**
 * Created by Ashish on Jan 08.
 */
class ImageViewerAdapter(fm: FragmentManager, val imageUrlList: ArrayList<String>?) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return ImageViewerFragment.newInstance(imageUrlList?.get(position))
    }

    override fun getCount() = imageUrlList?.size ?: 0
}