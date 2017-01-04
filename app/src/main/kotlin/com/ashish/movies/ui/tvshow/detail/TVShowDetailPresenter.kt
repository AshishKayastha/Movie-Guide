package com.ashish.movies.ui.tvshow.detail

import com.ashish.movies.data.interactors.TVShowInteractor
import com.ashish.movies.data.models.TVShowDetail
import com.ashish.movies.ui.base.detail.BaseDetailPresenter
import io.reactivex.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashish on Jan 03.
 */
class TVShowDetailPresenter @Inject constructor(val tvShowInteractor: TVShowInteractor)
    : BaseDetailPresenter<TVShowDetail, TVShowDetailMvpView>() {

    override fun getDetailContent(id: Long): Disposable {
        return tvShowInteractor.getTVShowDetailWithCreditsAndSimilarTVShows(id)
                .subscribe({ showTVShowDetailContents(it) }, { Timber.e(it) })
    }

    private fun showTVShowDetailContents(tvShowDetail: TVShowDetail?) {
        getView()?.apply {
            hideProgress()
            showDetailContent(tvShowDetail)

            val creditResults = tvShowDetail?.creditsResults
            showItemList(creditResults?.cast) { showCastList(it) }
            showItemList(creditResults?.crew) { showCrewList(it) }

            showItemList(tvShowDetail?.similarTVShowResults?.results) { showSimilarTVShowList(it) }
        }
    }
}