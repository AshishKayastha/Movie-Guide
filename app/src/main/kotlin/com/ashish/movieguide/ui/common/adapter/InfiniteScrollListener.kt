package com.ashish.movieguide.ui.common.adapter

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager

/**
 * Created by Ashish on Dec 29.
 */
class InfiniteScrollListener(private val loadMore: (currentPage: Int) -> Unit) : RecyclerView.OnScrollListener() {

    companion object {
        private const val VISIBLE_THRESHOLD = 2
    }

    var currentPage = 1
        get() = field
        set(value) {
            field = value
        }

    var shouldLoadMore = true
        set(value) {
            field = value
        }

    private var previousTotal = 0
    private var isLoading = true
    private var layoutManager: RecyclerView.LayoutManager? = null

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (shouldLoadMore) {
            if (layoutManager == null) layoutManager = recyclerView.layoutManager

            val totalItemCount = layoutManager!!.itemCount

            var lastVisibleItemPosition = 0
            if (layoutManager is StaggeredGridLayoutManager) {
                lastVisibleItemPosition = (layoutManager as StaggeredGridLayoutManager)
                        .findLastVisibleItemPositions(null)[0]

            } else if (layoutManager is LinearLayoutManager) {
                lastVisibleItemPosition = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            }

            // We need to consider extra loading/error item as well so, add 1 to previousTotal
            if (isLoading && totalItemCount > previousTotal + 1) {
                isLoading = false
                previousTotal = totalItemCount
            }

            if (!isLoading && lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
                currentPage++
                loadMore(currentPage)
                isLoading = true
            }
        }
    }

    fun stopLoading() {
        isLoading = false
        shouldLoadMore = false
        currentPage = if (currentPage > 1) currentPage-- else 1
    }

    fun resetPageCount(page: Int = 1) {
        isLoading = true
        shouldLoadMore = true
        previousTotal = 0
        currentPage = page
        loadMore(currentPage)
    }
}