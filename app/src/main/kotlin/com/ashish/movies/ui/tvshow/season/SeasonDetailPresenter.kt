package com.ashish.movies.ui.tvshow.season

import com.ashish.movies.R
import com.ashish.movies.data.interactors.TVShowInteractor
import com.ashish.movies.data.models.FullDetailContent
import com.ashish.movies.data.models.SeasonDetail
import com.ashish.movies.ui.base.detail.BaseDetailPresenter
import javax.inject.Inject

/**
 * Created by Ashish on Jan 07.
 */
class SeasonDetailPresenter @Inject constructor(val tvShowInteractor: TVShowInteractor)
    : BaseDetailPresenter<SeasonDetail, SeasonDetailMvpView>() {

    private var seasonNumber: Int = 1

    fun setSeasonNumber(seasonNumber: Int) {
        this.seasonNumber = seasonNumber
    }

    override fun getDetailContent(id: Long) = tvShowInteractor.getFullSeasonDetail(id, seasonNumber)

    override fun showDetailContent(fullDetailContent: FullDetailContent<SeasonDetail>) {
        super.showDetailContent(fullDetailContent)
        getView()?.apply {
            hideProgress()
            val seasonDetail = fullDetailContent.detailContent
            showItemList(seasonDetail?.episodes) { showEpisodeList(it) }
        }
    }

    override fun getCredits(detailContent: SeasonDetail?) = detailContent?.credits

    override fun getErrorMessageId() = R.string.error_load_season_detail
}