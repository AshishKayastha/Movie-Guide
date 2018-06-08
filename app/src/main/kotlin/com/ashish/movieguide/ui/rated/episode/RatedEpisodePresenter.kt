package com.ashish.movieguide.ui.rated.episode

import com.ashish.movieguide.data.interactors.AuthInteractor
import com.ashish.movieguide.data.network.entities.tmdb.Episode
import com.ashish.movieguide.data.network.entities.tmdb.Results
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewMvpView
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewPresenter
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import javax.inject.Inject

class RatedEpisodePresenter @Inject constructor(
        private val authInteractor: AuthInteractor,
        schedulerProvider: BaseSchedulerProvider
) : RecyclerViewPresenter<Episode, RecyclerViewMvpView<Episode>>(schedulerProvider) {

    override fun getResults(type: String?, page: Int): Single<Results<Episode>>
            = authInteractor.getRatedEpisodes(page)
}