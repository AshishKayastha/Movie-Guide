package com.ashish.movieguide.ui.discover.base

import com.ashish.movieguide.data.models.tmdb.FilterQuery
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movieguide.ui.common.adapter.ViewType

/**
 * Created by Ashish on Jan 24.
 */
interface DiscoverView<in I : ViewType> : BaseRecyclerViewMvpView<I> {

    fun clearFilteredData()

    fun showFilterBottomSheetDialog(filterQuery: FilterQuery)
}