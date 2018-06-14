package com.ashish.movieguide.ui.discover.base

import com.ashish.movieguide.data.remote.entities.tmdb.FilterQuery
import com.ashish.movieguide.ui.base.adapter.RecyclerViewItem
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewMvpView

/**
 * Created by Ashish on Jan 24.
 */
interface DiscoverView<in I : RecyclerViewItem> : RecyclerViewMvpView<I> {

    fun clearFilteredData()

    fun showFilterBottomSheetDialog(filterQuery: FilterQuery)
}