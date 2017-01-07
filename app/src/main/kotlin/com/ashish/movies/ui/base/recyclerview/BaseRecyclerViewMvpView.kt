package com.ashish.movies.ui.base.recyclerview

import com.ashish.movies.ui.base.mvp.LceView
import com.ashish.movies.ui.common.adapter.ViewType

/**
 * Created by Ashish on Dec 30.
 */
interface BaseRecyclerViewMvpView<in I : ViewType> : LceView {

    fun showItemList(itemList: List<I>?)

    fun showLoadingItem()

    fun addNewItemList(itemList: List<I>?)

    fun removeLoadingItem()

    fun resetLoading()
}