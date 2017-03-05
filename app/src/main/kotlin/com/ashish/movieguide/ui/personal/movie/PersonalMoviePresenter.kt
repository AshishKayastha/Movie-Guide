package com.ashish.movieguide.ui.personal.movie

import com.ashish.movieguide.data.interactors.AuthInteractor
import com.ashish.movieguide.data.models.Movie
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewPresenter
import com.ashish.movieguide.utils.Constants.PERSONAL_CONTENT_TYPES
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import javax.inject.Inject

class PersonalMoviePresenter @Inject constructor(
        private val authInteractor: AuthInteractor,
        schedulerProvider: BaseSchedulerProvider
) : BaseRecyclerViewPresenter<Movie, BaseRecyclerViewMvpView<Movie>>(schedulerProvider) {

    override fun getType(type: Int?) = PERSONAL_CONTENT_TYPES[type ?: 0]

    override fun getResultsObservable(type: String?, page: Int)
            = authInteractor.getPersonalMoviesByType(type!!, page)
}