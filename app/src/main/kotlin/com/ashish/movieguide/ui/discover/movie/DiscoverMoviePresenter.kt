package com.ashish.movieguide.ui.discover.movie

import com.ashish.movieguide.data.remote.entities.tmdb.Movie
import com.ashish.movieguide.data.remote.entities.tmdb.Results
import com.ashish.movieguide.data.remote.interactors.MovieInteractor
import com.ashish.movieguide.ui.discover.base.BaseDiscoverPresenter
import com.ashish.movieguide.ui.discover.filter.FilterQueryModel
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Ashish on Jan 06.
 */
class DiscoverMoviePresenter @Inject constructor(
        private val movieInteractor: MovieInteractor,
        filterQueryModel: FilterQueryModel,
        schedulerProvider: BaseSchedulerProvider
) : BaseDiscoverPresenter<Movie>(filterQueryModel, schedulerProvider) {

    override fun getResults(type: String?, page: Int): Single<Results<Movie>> {
        return movieInteractor.discoverMovie(sortBy, minDate, maxDate, genreIds, page)
    }
}