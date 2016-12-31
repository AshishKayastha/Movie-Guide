package com.ashish.movies.ui.tvshow

import com.ashish.movies.data.models.TVShow
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewAdapter
import com.ashish.movies.ui.common.LoadingDelegateAdapter
import com.ashish.movies.ui.common.ViewType.Companion.CONTENT_VIEW
import com.ashish.movies.ui.common.ViewType.Companion.LOADING_VIEW
import java.util.*

/**
 * Created by Ashish on Dec 30.
 */
class TVShowAdapter : BaseRecyclerViewAdapter<TVShow>() {

    init {
        delegateAdapters.put(LOADING_VIEW, LoadingDelegateAdapter())
        delegateAdapters.put(CONTENT_VIEW, TVShowDelegateAdapter())
        itemList = ArrayList()
        itemList.add(loadingItem)
    }
}