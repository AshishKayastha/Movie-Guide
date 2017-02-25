package com.ashish.movies.ui.discover.movie

import com.ashish.movies.data.interactors.MovieInteractor
import com.ashish.movies.data.models.Movie
import com.ashish.movies.data.models.Results
import com.ashish.movies.ui.discover.common.BaseDiscoverPresenter
import com.ashish.movies.ui.discover.common.filter.FilterQueryModel
import com.ashish.movies.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by Ashish on Jan 06.
 */
class DiscoverMoviePresenter @Inject constructor(
        private val movieInteractor: MovieInteractor,
        filterQueryModel: FilterQueryModel,
        schedulerProvider: BaseSchedulerProvider
) : BaseDiscoverPresenter<Movie>(filterQueryModel, schedulerProvider) {

    override fun getResultsObservable(type: String?, page: Int): Observable<Results<Movie>> {
        return movieInteractor.discoverMovie(sortBy, minDate, maxDate, genreIds, page)
    }
}