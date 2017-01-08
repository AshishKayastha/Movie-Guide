package com.ashish.movies.ui.tvshow.season

import com.ashish.movies.R
import com.ashish.movies.data.interactors.TVShowInteractor
import com.ashish.movies.data.models.SeasonDetail
import com.ashish.movies.ui.base.detail.BaseDetailPresenter
import io.reactivex.disposables.Disposable
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

    override fun getDetailContent(id: Long): Disposable {
        return tvShowInteractor.getSeasonDetail(id, seasonNumber)
                .subscribe({ showSeasonDetailContents(it) }, {
                    onLoadDetailError(it, R.string.error_load_season_detail)
                })
    }

    private fun showSeasonDetailContents(seasonDetail: SeasonDetail?) {
        getView()?.apply {
            hideProgress()
            showDetailContent(seasonDetail)
            showCredits(seasonDetail?.credits)
            showItemList(seasonDetail?.episodes) { showEpisodeList(it) }
        }
    }
}