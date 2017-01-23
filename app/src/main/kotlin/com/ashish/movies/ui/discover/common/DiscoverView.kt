package com.ashish.movies.ui.discover.common

import com.ashish.movies.data.models.FilterQuery
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movies.ui.common.adapter.ViewType
import io.reactivex.Observable

/**
 * Created by Ashish on Jan 24.
 */
interface DiscoverView<in I : ViewType> : BaseRecyclerViewMvpView<I> {

    fun getFilterQueryObservable(): Observable<FilterQuery>?

    fun showFilterBottomSheetDialog(filterQuery: FilterQuery)
}