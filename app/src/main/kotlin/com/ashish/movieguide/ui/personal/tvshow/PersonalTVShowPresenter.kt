package com.ashish.movieguide.ui.personal.tvshow

import com.ashish.movieguide.data.interactors.AuthInteractor
import com.ashish.movieguide.data.models.tmdb.Results
import com.ashish.movieguide.data.models.tmdb.TVShow
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewPresenter
import com.ashish.movieguide.utils.TMDbConstants.PERSONAL_CONTENT_TYPES
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import javax.inject.Inject

class PersonalTVShowPresenter @Inject constructor(
        private val authInteractor: AuthInteractor,
        schedulerProvider: BaseSchedulerProvider
) : BaseRecyclerViewPresenter<TVShow, BaseRecyclerViewMvpView<TVShow>>(schedulerProvider) {

    override fun getType(type: Int?) = PERSONAL_CONTENT_TYPES[type ?: 0]

    override fun getResults(type: String?, page: Int): Single<Results<TVShow>>
            = authInteractor.getPersonalTVShowsByType(type!!, page)
}