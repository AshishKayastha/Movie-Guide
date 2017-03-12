package com.ashish.movieguide.ui.rated.tvshow

import com.ashish.movieguide.data.interactors.AuthInteractor
import com.ashish.movieguide.data.models.tmdb.TVShow
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewPresenter
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import javax.inject.Inject

class RatedTVShowPresenter @Inject constructor(
        private val authInteractor: AuthInteractor,
        schedulerProvider: BaseSchedulerProvider
) : BaseRecyclerViewPresenter<TVShow, BaseRecyclerViewMvpView<TVShow>>(schedulerProvider) {

    override fun getResultsObservable(type: String?, page: Int) = authInteractor.getRatedTVShows(page)
}