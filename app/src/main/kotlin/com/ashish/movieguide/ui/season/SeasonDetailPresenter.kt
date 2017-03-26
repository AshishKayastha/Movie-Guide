package com.ashish.movieguide.ui.season

import com.ashish.movieguide.R
import com.ashish.movieguide.data.interactors.TVShowInteractor
import com.ashish.movieguide.data.network.entities.common.FullDetailContent
import com.ashish.movieguide.data.network.entities.tmdb.SeasonDetail
import com.ashish.movieguide.data.network.entities.trakt.TraktSeason
import com.ashish.movieguide.di.scopes.ActivityScope
import com.ashish.movieguide.ui.base.detail.fulldetail.FullDetailContentPresenter
import com.ashish.movieguide.utils.extensions.getFormattedMediumDate
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by Ashish on Jan 07.
 */
@ActivityScope
class SeasonDetailPresenter @Inject constructor(
        private val tvShowInteractor: TVShowInteractor,
        schedulerProvider: BaseSchedulerProvider
) : FullDetailContentPresenter<SeasonDetail, TraktSeason, SeasonDetailView>(schedulerProvider) {

    private var seasonNumber: Int = 1

    fun setSeasonNumber(seasonNumber: Int) {
        this.seasonNumber = seasonNumber
    }

    override fun getDetailContent(id: Long): Observable<FullDetailContent<SeasonDetail, TraktSeason>>
            = tvShowInteractor.getFullSeasonDetail(id, seasonNumber)

    override fun showDetailContent(fullDetailContent: FullDetailContent<SeasonDetail, TraktSeason>) {
        super.showDetailContent(fullDetailContent)
        getView()?.run {
            hideProgress()
            val seasonDetail = fullDetailContent.detailContent
            showItemList(seasonDetail?.episodes) { showEpisodeList(it) }
        }
    }

    override fun getContentList(fullDetailContent: FullDetailContent<SeasonDetail, TraktSeason>): List<String> {
        val contentList = ArrayList<String>()
        fullDetailContent.detailContent?.run {
            val omdbDetail = fullDetailContent.omdbDetail
            contentList.run {
                add(overview ?: "")
                add(omdbDetail?.Rated ?: "")
                add(omdbDetail?.Awards ?: "")
                add(seasonNumber.toString())
                add(episodes?.size.toString())
                add(airDate.getFormattedMediumDate())
                add(omdbDetail?.Production ?: "")
                add(omdbDetail?.Country ?: "")
                add(omdbDetail?.Language ?: "")
            }
        }

        return contentList
    }

    override fun getBackdropImages(detailContent: SeasonDetail) = detailContent.images?.backdrops

    override fun getPosterImages(detailContent: SeasonDetail) = detailContent.images?.posters

    override fun getVideos(detailContent: SeasonDetail) = detailContent.videos

    override fun getCredits(detailContent: SeasonDetail) = detailContent.credits

    override fun getErrorMessageId() = R.string.error_load_season_detail
}