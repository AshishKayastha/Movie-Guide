package com.ashish.movies.ui.movie

import com.ashish.movies.data.models.Movie
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewAdapter
import com.ashish.movies.ui.common.LoadingDelegateAdapter
import com.ashish.movies.ui.common.ViewType.Companion.CONTENT_VIEW
import com.ashish.movies.ui.common.ViewType.Companion.LOADING_VIEW
import java.util.*

/**
 * Created by Ashish on Dec 27.
 */
class MovieAdapter : BaseRecyclerViewAdapter<Movie>() {

    init {
        delegateAdapters.put(LOADING_VIEW, LoadingDelegateAdapter())
        delegateAdapters.put(CONTENT_VIEW, MovieDelegateAdapter())
        itemList = ArrayList()
        itemList.add(loadingItem)
    }
}