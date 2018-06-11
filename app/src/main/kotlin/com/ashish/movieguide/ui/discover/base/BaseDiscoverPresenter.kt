package com.ashish.movieguide.ui.discover.base

import com.ashish.movieguide.data.network.entities.tmdb.FilterQuery
import com.ashish.movieguide.ui.base.adapter.RecyclerViewItem
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewPresenter
import com.ashish.movieguide.ui.discover.filter.FilterQueryModel
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider

/**
 * Created by Ashish on Jan 24.
 */
abstract class BaseDiscoverPresenter<I : RecyclerViewItem> constructor(
        private val filterQueryModel: FilterQueryModel,
        schedulerProvider: BaseSchedulerProvider
) : RecyclerViewPresenter<I, DiscoverView<I>>(schedulerProvider) {

    protected var minDate: String? = null
    protected var maxDate: String? = null
    protected var genreIds: String? = null
    protected var sortBy: String = "popularity.desc"

    fun filterContents() {
        addDisposable(filterQueryModel.getFilterQuery()
                .filter { isValidFilterQuery(it) }
                .subscribe { (sortBy, genreIds, minDate, maxDate) ->
                    this.sortBy = sortBy
                    this.minDate = minDate
                    this.maxDate = maxDate
                    this.genreIds = genreIds

                    view?.clearFilteredData()
                    fetchFreshData(null)
                })
    }

    private fun isValidFilterQuery(filterQuery: FilterQuery): Boolean {
        return filterQuery.genreIds.isNotNullOrEmpty()
                || filterQuery.minDate.isNotNullOrEmpty()
                || filterQuery.maxDate.isNotNullOrEmpty()
    }

    fun onFilterMenuItemClick() {
        val filterQuery = FilterQuery(sortBy, genreIds, minDate, maxDate)
        view?.showFilterBottomSheetDialog(filterQuery)
    }
}