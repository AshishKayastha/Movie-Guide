package com.ashish.movies.ui.discover.tvshow

import com.ashish.movies.data.interactors.TVShowInteractor
import com.ashish.movies.data.models.Results
import com.ashish.movies.data.models.TVShow
import com.ashish.movies.ui.discover.common.BaseDiscoverPresenter
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by Ashish on Jan 06.
 */
class DiscoverTVShowPresenter @Inject constructor(private val tvShowInteractor: TVShowInteractor)
    : BaseDiscoverPresenter<TVShow>() {

    override fun getResultsObservable(type: String?, page: Int): Observable<Results<TVShow>> {
        return tvShowInteractor.discoverTVShow(sortBy, year, genreIds, page)
    }
}