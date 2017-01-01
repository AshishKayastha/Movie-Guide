package com.ashish.movies.ui.tvshow

import com.ashish.movies.data.api.TVShowService.Companion.AIRING_TODAY
import com.ashish.movies.data.api.TVShowService.Companion.ON_THE_AIR
import com.ashish.movies.data.api.TVShowService.Companion.POPULAR
import com.ashish.movies.data.api.TVShowService.Companion.TOP_RATED
import com.ashish.movies.data.interactors.TVShowInteractor
import com.ashish.movies.data.models.TVShow
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewPresenter
import javax.inject.Inject

/**
 * Created by Ashish on Dec 30.
 */
class TVShowPresenter @Inject constructor(val tvShowInteractor: TVShowInteractor)
    : BaseRecyclerViewPresenter<TVShow, BaseRecyclerViewMvpView<TVShow>>() {

    companion object {
        private val TV_SHOW_TYPES = arrayOf(ON_THE_AIR, POPULAR, TOP_RATED, AIRING_TODAY)
    }

    override fun loadData(type: Int?, page: Int, showProgress: Boolean) {
        getDataByType(TV_SHOW_TYPES[type ?: 0], page, showProgress)
    }

    override fun loadMoreData(type: Int?, page: Int) {
        if (page <= totalPages) getMoreDataByType(TV_SHOW_TYPES[type ?: 0], page)
    }

    override fun getData(type: String?, page: Int) = tvShowInteractor.getTVShowsByType(type, page)
}