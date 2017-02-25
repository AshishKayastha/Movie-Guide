package com.ashish.movies.ui.discover.tvshow

import com.ashish.movies.data.interactors.TVShowInteractor
import com.ashish.movies.data.models.Results
import com.ashish.movies.data.models.TVShow
import com.ashish.movies.ui.discover.base.BaseDiscoverPresenter
import com.ashish.movies.ui.discover.filter.FilterQueryModel
import com.ashish.movies.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by Ashish on Jan 06.
 */
class DiscoverTVShowPresenter @Inject constructor(
        private val tvShowInteractor: TVShowInteractor,
        filterQueryModel: FilterQueryModel,
        schedulerProvider: BaseSchedulerProvider
) : BaseDiscoverPresenter<TVShow>(filterQueryModel, schedulerProvider) {

    override fun getResultsObservable(type: String?, page: Int): Observable<Results<TVShow>> {
        return tvShowInteractor.discoverTVShow(sortBy, minDate, maxDate, genreIds, page)
    }
}