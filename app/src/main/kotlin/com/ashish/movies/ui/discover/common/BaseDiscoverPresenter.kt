package com.ashish.movies.ui.discover.common

import com.ashish.movies.data.models.FilterQuery
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewPresenter
import com.ashish.movies.ui.common.adapter.ViewType
import com.ashish.movies.utils.extensions.isNotNullOrEmpty
import timber.log.Timber

/**
 * Created by Ashish on Jan 24.
 */
abstract class BaseDiscoverPresenter<I : ViewType> : BaseRecyclerViewPresenter<I, DiscoverView<I>>() {

    protected var year: Int = 2016
    protected var genreIds: String? = null
    protected var sortBy: String = "popularity.desc"

    fun filterContents() {
        val filterQueryObservable = getView()?.getFilterQueryObservable()
        if (filterQueryObservable != null) {
            addDisposable(filterQueryObservable
                    .doOnNext { Timber.v("Genre Ids: " + it.genreIds) }
                    .filter { it.genreIds.isNotNullOrEmpty() }
                    .subscribe {
                        this.year = it.year
                        this.genreIds = it.genreIds
                        loadFreshData(null, true)
                    })
        }
    }

    override fun getType(type: Int?) = null

    fun onFilterMenuItemClick() = getView()?.showFilterBottomSheetDialog(FilterQuery(genreIds, year))
}