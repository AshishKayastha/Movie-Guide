package com.ashish.movies.ui.common.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager

/**
 * Created by Ashish on Dec 29.
 */
abstract class InfiniteScrollListener : RecyclerView.OnScrollListener() {

    companion object {
        const val VISIBLE_THRESHOLD = 2
    }

    private var currentPage = 1
    private var previousTotal = 0
    private var lastVisibleItems = IntArray(2)

    private var isLoading = true
    private var layoutManager: RecyclerView.LayoutManager? = null

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (layoutManager == null) layoutManager = recyclerView.layoutManager

        val totalItemCount = layoutManager!!.itemCount
        lastVisibleItems = (layoutManager as StaggeredGridLayoutManager)
                .findLastVisibleItemPositions(lastVisibleItems)

        if (isLoading && totalItemCount > previousTotal + 1) {
            isLoading = false
            previousTotal = totalItemCount
        }

        if (!isLoading && totalItemCount <= lastVisibleItems[0] + VISIBLE_THRESHOLD) {
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