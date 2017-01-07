package com.ashish.movies.ui.tvshow.season

import com.ashish.movies.data.interactors.TVShowInteractor
import com.ashish.movies.data.models.SeasonDetail
import com.ashish.movies.ui.base.detail.BaseDetailPresenter
import io.reactivex.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashish on Jan 07.
 */
class SeasonDetailPresenter @Inject constructor(val tvShowInteractor: TVShowInteractor)
    : BaseDetailPresenter<SeasonDetail, SeasonDetailMvpView>() {

    private var seasoNumber: Int = 1

    fun setSeasonNumber(seasonNumber: Int) {
        this.seasoNumber = seasoNumber
    }

    override fun getDetailContent(id: Long): Disposable {
        return tvShowInteractor.getSeasonDetail(id, seasoNumber)
                .subscribe({ showSeasonDetailContents(it) }, { Timber.e(it) })
    }

    private fun showSeasonDetailContents(seasonDetail: SeasonDetail?) {
        getView()?.apply {
            hideProgress()
            showDetailContent(seasonDetail)

            val creditResults = seasonDetail?.credits
            showItemList(creditResults?.cast) { showCastList(it) }
            showItemList(creditResults?.crew) { showCrewList(it) }

            showItemList(seasonDetail?.episodes) { showEpisodeList(it) }
        }
    }
}