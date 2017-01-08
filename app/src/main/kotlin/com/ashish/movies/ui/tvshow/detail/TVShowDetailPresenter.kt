package com.ashish.movies.ui.tvshow.detail

import com.ashish.movies.R
import com.ashish.movies.data.interactors.TVShowInteractor
import com.ashish.movies.data.models.TVShowDetail
import com.ashish.movies.ui.base.detail.BaseDetailPresenter
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * Created by Ashish on Jan 03.
 */
class TVShowDetailPresenter @Inject constructor(val tvShowInteractor: TVShowInteractor)
    : BaseDetailPresenter<TVShowDetail, TVShowDetailMvpView>() {

    override fun getDetailContent(id: Long): Disposable {
        return tvShowInteractor.getTVShowDetailWithCreditsAndSimilarTVShows(id)
                .subscribe({ showTVShowDetailContents(it) }, { onLoadDetailError(it, R.string.error_load_tv_detail) })
    }

    private fun showTVShowDetailContents(tvShowDetail: TVShowDetail?) {
        getView()?.apply {
            hideProgress()
            showDetailContent(tvShowDetail)
            showCredits(tvShowDetail?.creditsResults)
            showItemList(tvShowDetail?.seasons) { showSeasonsList(it) }
            showItemList(tvShowDetail?.similarTVShowResults?.results) { showSimilarTVShowList(it) }
        }
    }
}