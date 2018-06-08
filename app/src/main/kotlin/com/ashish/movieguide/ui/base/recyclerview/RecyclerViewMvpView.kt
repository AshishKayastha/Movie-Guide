package com.ashish.movieguide.ui.base.recyclerview

import com.ashish.movieguide.ui.base.mvp.ProgressMvpView
import com.ashish.movieguide.ui.common.adapter.ViewType
import net.grandcentrix.thirtyinch.distinctuntilchanged.DistinctUntilChanged

interface RecyclerViewMvpView<in I : ViewType> : ProgressMvpView {

    @DistinctUntilChanged
    fun setCurrentPage(currentPage: Int)

    @DistinctUntilChanged
    fun showItemList(itemList: List<I>?)

    fun showLoadingItem()

    @DistinctUntilChanged
    fun addNewItemList(itemList: List<I>?)

    fun removeLoadingItem()

    fun showErrorView()

    fun resetLoading()
}