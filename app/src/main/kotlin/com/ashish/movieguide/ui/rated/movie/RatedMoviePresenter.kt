package com.ashish.movieguide.ui.rated.movie

import com.ashish.movieguide.data.interactors.AuthInteractor
import com.ashish.movieguide.data.network.entities.tmdb.Movie
import com.ashish.movieguide.data.network.entities.tmdb.Results
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewMvpView
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewPresenter
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import javax.inject.Inject

class RatedMoviePresenter @Inject constructor(
        private val authInteractor: AuthInteractor,
        schedulerProvider: BaseSchedulerProvider
) : RecyclerViewPresenter<Movie, RecyclerViewMvpView<Movie>>(schedulerProvider) {

    override fun getResults(type: String?, page: Int): Single<Results<Movie>> =
            authInteractor.getRatedMovies(page)
}