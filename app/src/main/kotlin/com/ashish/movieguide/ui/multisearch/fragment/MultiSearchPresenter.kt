package com.ashish.movieguide.ui.multisearch.fragment

import com.ashish.movieguide.data.api.tmdb.SearchApi
import com.ashish.movieguide.data.models.tmdb.MultiSearch
import com.ashish.movieguide.data.models.tmdb.Results
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewPresenter
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Ashish on Jan 05.
 */
class MultiSearchPresenter @Inject constructor(
        private val searchApi: SearchApi,
        schedulerProvider: BaseSchedulerProvider
) : BaseRecyclerViewPresenter<MultiSearch, BaseRecyclerViewMvpView<MultiSearch>>(schedulerProvider) {

    private var searchQuery = ""

    fun setSearchQuery(searchQuery: String) {
        this.searchQuery = searchQuery
    }

    override fun getType(type: Int?) = searchQuery

    override fun getResults(type: String?, page: Int): Single<Results<MultiSearch>>
            = searchApi.getMultiSearchResults(type!!, page)
}