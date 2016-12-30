package com.ashish.movies.ui.common

import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by Ashish on Dec 29.
 */
abstract class RecyclerViewScrollListener : RecyclerView.OnScrollListener() {

    private var currentPage = 1
    private var previousTotal = 0
    private var visibleThreshold = -1

    private var isLoading = true
    private var isOrientationHelperVertical: Boolean = false

    private var orientationHelper: OrientationHelper? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    private fun findFirstVisibleItemPosition(recyclerView: RecyclerView): Int {
        val child = findOneVisibleChild(0, layoutManager!!.childCount, false, true)
        return if (child == null) RecyclerView.NO_POSITION else recyclerView.getChildAdapterPosition(child)
    }

    private fun findLastVisibleItemPosition(recyclerView: RecyclerView): Int {
        val child = findOneVisibleChild(recyclerView.childCount - 1, -1, false, true)
        return if (child == null) RecyclerView.NO_POSITION else recyclerView.getChildAdapterPosition(child)
    }

    private fun findOneVisibleChild(fromIndex: Int, toIndex: Int, completelyVisible: Boolean,
                                    acceptPartiallyVisible: Boolean): View? {
        if (layoutManager!!.canScrollVertically() != isOrientationHelperVertical || orientationHelper == null) {
            isOrientationHelperVertical = layoutManager!!.canScrollVertically()

            orientationHelper = if (isOrientationHelperVertical)
                OrientationHelper.createVerticalHelper(layoutManager)
            else
                OrientationHelper.createHorizontalHelper(layoutManager)
        }

        val start = orientationHelper!!.startAfterPadding
        val end = orientationHelper!!.endAfterPadding
        val next = if (toIndex > fromIndex) 1 else -1

        var partiallyVisible: View? = null

        var i = fromIndex
        while (i != toIndex) {
            val child = layoutManager!!.getChildAt(i)
            if (child != null) {
                val childStart = orientationHelper!!.getDecoratedStart(child)
                val childEnd = orientationHelper!!.getDecoratedEnd(child)

                if (childStart < end && childEnd > start) {
                    if (completelyVisible) {
                        if (childStart >= start && childEnd <= end) {
                            return child
                        } else if (acceptPartiallyVisible && partiallyVisible == null) {
                            partiallyVisible = child
                        }
                    } else {
                        return child
                    }
                }
            }
            i += next
        }

        return partiallyVisible
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (layoutManager == null) layoutManager = recyclerView.layoutManager

        if (visibleThreshold == -1) {
            visibleThreshold = findLastVisibleItemPosition(recyclerView) - findFirstVisibleItemPosition(recyclerView)
        }

        val firstVisibleItem = findFirstVisibleItemPosition(recyclerView)
        val visibleItemCount = recyclerView.childCount
        val totalItemCount = layoutManager!!.itemCount

        if (isLoading && totalItemCount > previousTotal) {
            isLoading = false
            previousTotal = totalItemCount
        }

        if (!isLoading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
            currentPage++
            onLoadMore(currentPage)
            isLoading = true
        }
    }

    fun resetLoading() {
        currentPage = if (currentPage > 1) currentPage-- else 1
        isLoading = false
    }

    fun resetPageCount(page: Int = 1) {
        previousTotal = 0
        isLoading = true
        currentPage = page
        onLoadMore(currentPage)
    }

    abstract fun onLoadMore(currentPage: Int)
}