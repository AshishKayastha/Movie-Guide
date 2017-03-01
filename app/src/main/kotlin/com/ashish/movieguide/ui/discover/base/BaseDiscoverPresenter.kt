package com.ashish.movieguide.ui.discover.base

import com.ashish.movieguide.data.models.FilterQuery
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewPresenter
import com.ashish.movieguide.ui.common.adapter.ViewType
import com.ashish.movieguide.ui.discover.filter.FilterQueryModel
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider

/**
 * Created by Ashish on Jan 24.
 */
abstract class BaseDiscoverPresenter<I : ViewType> constructor(
        private val filterQueryModel: FilterQueryModel,
        schedulerProvider: BaseSchedulerProvider
) : BaseRecyclerViewPresenter<I, DiscoverView<I>>(schedulerProvider) {

    protected var minDate: String? = null
    protected var maxDate: String? = null
    protected var genreIds: String? = null
    protected var sortBy: String = "popularity.desc"

    fun filterContents() {
        addDisposable(filterQueryModel.getFilterQuery()
                .filter { isValidFilterQuery(it) }
                .subscribe { filterQuery ->
                    with(filterQuery) {
                        this.sortBy = sortBy
                        this.minDate = minDate
                        this.maxDate = maxDate
                        this.genreIds = genreIds
                    }

                    getView()?.clearFilteredData()
                    loadFreshData(null, true)
                })
    }

    private fun isValidFilterQuery(filterQuery: FilterQuery): Boolean {
        return filterQuery.genreIds.isNotNullOrEmpty()
                || filterQuery.minDate.isNotNullOrEmpty()
                || filterQuery.maxDate.isNotNullOrEmpty()
    }

    override fun getType(type: Int?) = null

    fun onFilterMenuItemClick() {
        val filterQuery = FilterQuery(sortBy, genreIds, minDate, maxDate)
        getView()?.showFilterBottomSheetDialog(filterQuery)
    }
}