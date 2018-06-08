package com.ashish.movieguide.ui.discover.base

import com.ashish.movieguide.data.network.entities.tmdb.FilterQuery
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewMvpView
import com.ashish.movieguide.ui.common.adapter.ViewType

/**
 * Created by Ashish on Jan 24.
 */
interface DiscoverView<in I : ViewType> : RecyclerViewMvpView<I> {

    fun clearFilteredData()

    fun showFilterBottomSheetDialog(filterQuery: FilterQuery)
}