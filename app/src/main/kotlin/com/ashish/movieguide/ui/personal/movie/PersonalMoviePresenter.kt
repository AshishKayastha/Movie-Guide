package com.ashish.movieguide.ui.personal.movie

import com.ashish.movieguide.data.interactors.AuthInteractor
import com.ashish.movieguide.data.network.entities.tmdb.Movie
import com.ashish.movieguide.data.network.entities.tmdb.Results
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewMvpView
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewPresenter
import com.ashish.movieguide.utils.TMDbConstants.PERSONAL_CONTENT_TYPES
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import javax.inject.Inject

class PersonalMoviePresenter @Inject constructor(
        private val authInteractor: AuthInteractor,
        schedulerProvider: BaseSchedulerProvider
) : RecyclerViewPresenter<Movie, RecyclerViewMvpView<Movie>>(schedulerProvider) {

    override fun getType(type: Int?): String? = PERSONAL_CONTENT_TYPES[type ?: 0]

    override fun getResults(type: String?, page: Int): Single<Results<Movie>> =
            authInteractor.getPersonalMoviesByType(type!!, page)
}