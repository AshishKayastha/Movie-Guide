package com.ashish.movieguide.ui.multisearch.fragment

import com.ashish.movieguide.data.remote.api.tmdb.SearchApi
import com.ashish.movieguide.data.remote.entities.tmdb.MultiSearch
import com.ashish.movieguide.data.remote.entities.tmdb.Results
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewMvpView
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewPresenter
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Ashish on Jan 05.
 */
class MultiSearchPresenter @Inject constructor(
        private val searchApi: SearchApi,
        schedulerProvider: BaseSchedulerProvider
) : RecyclerViewPresenter<MultiSearch, RecyclerViewMvpView<MultiSearch>>(schedulerProvider) {

    private var searchQuery = ""

    fun setSearchQuery(searchQuery: String) {
        this.searchQuery = searchQuery
    }

    override fun getType(type: Int?): String? = searchQuery

    override fun getResults(type: String?, page: Int): Single<Results<MultiSearch>> =
            searchApi.getMultiSearchResults(type!!, page)
}