package com.ashish.movieguide.ui.rated.episode

import com.ashish.movieguide.data.interactors.AuthInteractor
import com.ashish.movieguide.data.models.tmdb.Episode
import com.ashish.movieguide.data.models.tmdb.Results
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewPresenter
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import javax.inject.Inject

class RatedEpisodePresenter @Inject constructor(
        private val authInteractor: AuthInteractor,
        schedulerProvider: BaseSchedulerProvider
) : BaseRecyclerViewPresenter<Episode, BaseRecyclerViewMvpView<Episode>>(schedulerProvider) {

    override fun getResults(type: String?, page: Int): Single<Results<Episode>>
            = authInteractor.getRatedEpisodes(page)
}