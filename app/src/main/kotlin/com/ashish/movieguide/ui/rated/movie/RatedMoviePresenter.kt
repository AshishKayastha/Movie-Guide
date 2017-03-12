package com.ashish.movieguide.ui.rated.movie

import com.ashish.movieguide.data.interactors.AuthInteractor
import com.ashish.movieguide.data.models.tmdb.Movie
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewPresenter
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import javax.inject.Inject

class RatedMoviePresenter @Inject constructor(
        private val authInteractor: AuthInteractor,
        schedulerProvider: BaseSchedulerProvider
) : BaseRecyclerViewPresenter<Movie, BaseRecyclerViewMvpView<Movie>>(schedulerProvider) {

    override fun getResultsObservable(type: String?, page: Int) = authInteractor.getRatedMovies(page)
}