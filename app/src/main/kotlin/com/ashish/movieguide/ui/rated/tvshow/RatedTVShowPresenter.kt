package com.ashish.movieguide.ui.rated.tvshow

import com.ashish.movieguide.data.remote.entities.tmdb.Results
import com.ashish.movieguide.data.remote.entities.tmdb.TVShow
import com.ashish.movieguide.data.remote.interactors.AuthInteractor
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewMvpView
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewPresenter
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import javax.inject.Inject

class RatedTVShowPresenter @Inject constructor(
        private val authInteractor: AuthInteractor,
        schedulerProvider: BaseSchedulerProvider
) : RecyclerViewPresenter<TVShow, RecyclerViewMvpView<TVShow>>(schedulerProvider) {

    override fun getResults(type: String?, page: Int): Single<Results<TVShow>> =
            authInteractor.getRatedTVShows(page)
}