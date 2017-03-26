package com.ashish.movieguide.ui.discover.tvshow

import com.ashish.movieguide.data.interactors.TVShowInteractor
import com.ashish.movieguide.data.network.entities.tmdb.Results
import com.ashish.movieguide.data.network.entities.tmdb.TVShow
import com.ashish.movieguide.ui.discover.base.BaseDiscoverPresenter
import com.ashish.movieguide.ui.discover.filter.FilterQueryModel
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Ashish on Jan 06.
 */
class DiscoverTVShowPresenter @Inject constructor(
        private val tvShowInteractor: TVShowInteractor,
        filterQueryModel: FilterQueryModel,
        schedulerProvider: BaseSchedulerProvider
) : BaseDiscoverPresenter<TVShow>(filterQueryModel, schedulerProvider) {

    override fun getResults(type: String?, page: Int): Single<Results<TVShow>> {
        return tvShowInteractor.discoverTVShow(sortBy, minDate, maxDate, genreIds, page)
    }
}