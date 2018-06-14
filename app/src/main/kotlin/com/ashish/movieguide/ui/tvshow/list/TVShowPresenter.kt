package com.ashish.movieguide.ui.tvshow.list

import com.ashish.movieguide.data.remote.api.tmdb.TVShowApi.Companion.AIRING_TODAY
import com.ashish.movieguide.data.remote.api.tmdb.TVShowApi.Companion.ON_THE_AIR
import com.ashish.movieguide.data.remote.api.tmdb.TVShowApi.Companion.POPULAR
import com.ashish.movieguide.data.remote.api.tmdb.TVShowApi.Companion.TOP_RATED
import com.ashish.movieguide.data.remote.entities.tmdb.Results
import com.ashish.movieguide.data.remote.entities.tmdb.TVShow
import com.ashish.movieguide.data.remote.interactors.TVShowInteractor
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewMvpView
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewPresenter
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Ashish on Dec 30.
 */
class TVShowPresenter @Inject constructor(
        private val tvShowInteractor: TVShowInteractor,
        schedulerProvider: BaseSchedulerProvider
) : RecyclerViewPresenter<TVShow, RecyclerViewMvpView<TVShow>>(schedulerProvider) {

    companion object {
        private val TV_SHOW_TYPES = arrayOf(ON_THE_AIR, POPULAR, TOP_RATED, AIRING_TODAY)
    }

    override fun getType(type: Int?): String? = TV_SHOW_TYPES[type ?: 0]

    override fun getResults(type: String?, page: Int): Single<Results<TVShow>> =
            tvShowInteractor.getTVShowsByType(type, page)
}