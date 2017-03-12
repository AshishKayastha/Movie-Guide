package com.ashish.movieguide.ui.personal.movie

import com.ashish.movieguide.data.interactors.AuthInteractor
import com.ashish.movieguide.data.models.tmdb.Movie
import com.ashish.movieguide.data.models.tmdb.Results
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewPresenter
import com.ashish.movieguide.utils.Constants.PERSONAL_CONTENT_TYPES
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import javax.inject.Inject

class PersonalMoviePresenter @Inject constructor(
        private val authInteractor: AuthInteractor,
        schedulerProvider: BaseSchedulerProvider
) : BaseRecyclerViewPresenter<Movie, BaseRecyclerViewMvpView<Movie>>(schedulerProvider) {

    override fun getType(type: Int?) = PERSONAL_CONTENT_TYPES[type ?: 0]

    override fun getResults(type: String?, page: Int): Single<Results<Movie>>
            = authInteractor.getPersonalMoviesByType(type!!, page)
}