package com.ashish.movies.ui.tvshow.detail

import com.ashish.movies.R
import com.ashish.movies.data.interactors.TVShowInteractor
import com.ashish.movies.data.models.FullDetailContent
import com.ashish.movies.data.models.TVShowDetail
import com.ashish.movies.ui.base.detail.BaseDetailPresenter
import javax.inject.Inject

/**
 * Created by Ashish on Jan 03.
 */
class TVShowDetailPresenter @Inject constructor(val tvShowInteractor: TVShowInteractor)
    : BaseDetailPresenter<TVShowDetail, TVShowDetailMvpView>() {

    override fun getDetailContent(id: Long) = tvShowInteractor.getFullTVShowDetail(id)

    override fun showDetailContent(fullDetailContent: FullDetailContent<TVShowDetail>) {
        super.showDetailContent(fullDetailContent)
        getView()?.apply {
            hideProgress()
            val tvShowDetail = fullDetailContent.detailContent
            showItemList(tvShowDetail?.seasons) { showSeasonsList(it) }
            showItemList(tvShowDetail?.similarTVShowResults?.results) { showSimilarTVShowList(it) }
        }
    }

    override fun getCredits(detailContent: TVShowDetail?) = detailContent?.creditsResults

    override fun getErrorMessageId() = R.string.error_load_tv_detail
}