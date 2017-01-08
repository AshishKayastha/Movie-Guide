package com.ashish.movies.ui.discover.tvshow

import com.ashish.movies.data.interactors.TVShowInteractor
import com.ashish.movies.data.models.Results
import com.ashish.movies.data.models.TVShow
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewPresenter
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by Ashish on Jan 06.
 */
class DiscoverTVShowPresenter @Inject constructor(val tvShowInteractor: TVShowInteractor)
    : BaseRecyclerViewPresenter<TVShow, BaseRecyclerViewMvpView<TVShow>>() {

    private var genres: String? = null
    private var firstAirDateYear: Int = 2016
    private var sortBy: String = "popularity.desc"

    fun setFilterQueries(sortBy: String, firstAirDateYear: Int, genres: String? = null) {
        this.sortBy = sortBy
        this.firstAirDateYear = firstAirDateYear
        this.genres = genres
    }

    override fun getResultsObservable(type: String?, page: Int): Observable<Results<TVShow>> {
        return tvShowInteractor.discoverTVShow(sortBy, firstAirDateYear, genres, page)
    }
}