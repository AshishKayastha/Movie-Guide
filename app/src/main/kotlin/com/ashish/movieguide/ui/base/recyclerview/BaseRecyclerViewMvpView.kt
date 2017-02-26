package com.ashish.movieguide.ui.base.recyclerview

import com.ashish.movieguide.ui.base.mvp.ProgressView
import com.ashish.movieguide.ui.common.adapter.ViewType

/**
 * Created by Ashish on Dec 30.
 */
interface BaseRecyclerViewMvpView<in I : ViewType> : ProgressView {

    fun setCurrentPage(currentPage: Int)

    fun showItemList(itemList: List<I>?)

    fun showLoadingItem()

    fun addNewItemList(itemList: List<I>?)

    fun removeLoadingItem()

    fun resetLoading()
}