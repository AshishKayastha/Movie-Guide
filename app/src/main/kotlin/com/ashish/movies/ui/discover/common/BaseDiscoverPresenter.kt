package com.ashish.movies.ui.discover.common

import com.ashish.movies.data.models.FilterQuery
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewPresenter
import com.ashish.movies.ui.common.adapter.ViewType
import com.ashish.movies.ui.discover.common.filter.FilterQueryModel
import com.ashish.movies.utils.extensions.isNotNullOrEmpty

/**
 * Created by Ashish on Jan 24.
 */
abstract class BaseDiscoverPresenter<I : ViewType> constructor(private val filterQueryModel: FilterQueryModel)
    : BaseRecyclerViewPresenter<I, DiscoverView<I>>() {

    protected var minDate: String? = null
    protected var maxDate: String? = null
    protected var genreIds: String? = null
    protected var sortBy: String = "popularity.desc"

    fun filterContents() {
        addDisposable(filterQueryModel.getFilterQuery()
                .filter { it.genreIds.isNotNullOrEmpty() }
                .subscribe {
                    this.minDate = it.minDate
                    this.maxDate = it.maxDate
                    this.genreIds = it.genreIds
                    getView()?.clearFilteredData()
                    loadFreshData(null, true)
                })
    }

    override fun getType(type: Int?) = null

    fun onFilterMenuItemClick() = getView()?.showFilterBottomSheetDialog(FilterQuery(genreIds, minDate, maxDate))
}