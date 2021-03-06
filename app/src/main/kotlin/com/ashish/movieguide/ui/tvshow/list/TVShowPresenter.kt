package com.ashish.movieguide.ui.tvshow.list

import com.ashish.movieguide.data.api.TVShowApi.Companion.AIRING_TODAY
import com.ashish.movieguide.data.api.TVShowApi.Companion.ON_THE_AIR
import com.ashish.movieguide.data.api.TVShowApi.Companion.POPULAR
import com.ashish.movieguide.data.api.TVShowApi.Companion.TOP_RATED
import com.ashish.movieguide.data.interactors.TVShowInteractor
import com.ashish.movieguide.data.models.TVShow
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewPresenter
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import javax.inject.Inject

/**
 * Created by Ashish on Dec 30.
 */
class TVShowPresenter @Inject constructor(
        private val tvShowInteractor: TVShowInteractor,
        schedulerProvider: BaseSchedulerProvider
) : BaseRecyclerViewPresenter<TVShow, BaseRecyclerViewMvpView<TVShow>>(schedulerProvider) {

    companion object {
        @JvmStatic
        private val TV_SHOW_TYPES = arrayOf(ON_THE_AIR, POPULAR, TOP_RATED, AIRING_TODAY)
    }

    override fun getType(type: Int?) = TV_SHOW_TYPES[type ?: 0]

    override fun getResultsObservable(type: String?, page: Int) = tvShowInteractor.getTVShowsByType(type, page)
}