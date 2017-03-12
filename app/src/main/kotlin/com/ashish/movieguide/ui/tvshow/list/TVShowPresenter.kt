package com.ashish.movieguide.ui.tvshow.list

import com.ashish.movieguide.data.api.tmdb.TVShowApi.Companion.AIRING_TODAY
import com.ashish.movieguide.data.api.tmdb.TVShowApi.Companion.ON_THE_AIR
import com.ashish.movieguide.data.api.tmdb.TVShowApi.Companion.POPULAR
import com.ashish.movieguide.data.api.tmdb.TVShowApi.Companion.TOP_RATED
import com.ashish.movieguide.data.interactors.TVShowInteractor
import com.ashish.movieguide.data.models.tmdb.Results
import com.ashish.movieguide.data.models.tmdb.TVShow
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewPresenter
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
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

    override fun getResults(type: String?, page: Int): Single<Results<TVShow>>
            = tvShowInteractor.getTVShowsByType(type, page)
}