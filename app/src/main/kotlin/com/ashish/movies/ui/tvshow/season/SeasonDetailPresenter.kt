package com.ashish.movies.ui.tvshow.season

import com.ashish.movies.R
import com.ashish.movies.data.interactors.TVShowInteractor
import com.ashish.movies.data.models.FullDetailContent
import com.ashish.movies.data.models.SeasonDetail
import com.ashish.movies.ui.base.detail.BaseDetailPresenter
import com.ashish.movies.utils.extensions.getFormattedMediumDate
import java.util.*
import javax.inject.Inject

/**
 * Created by Ashish on Jan 07.
 */
class SeasonDetailPresenter @Inject constructor(private val tvShowInteractor: TVShowInteractor)
    : BaseDetailPresenter<SeasonDetail, SeasonDetailView>() {

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

    override fun getContentList(fullDetailContent: FullDetailContent<SeasonDetail>): List<String> {
        val contentList = ArrayList<String>()
        fullDetailContent.detailContent?.apply {
            val omdbDetail = fullDetailContent.omdbDetail
            contentList.add(overview ?: "")
            contentList.add(omdbDetail?.Rated ?: "")
            contentList.add(omdbDetail?.Awards ?: "")
            contentList.add(seasonNumber.toString())
            contentList.add(episodes?.size.toString())
            contentList.add(airDate.getFormattedMediumDate())
            contentList.add(omdbDetail?.Production ?: "")
            contentList.add(omdbDetail?.Country ?: "")
            contentList.add(omdbDetail?.Language ?: "")
        }

        return contentList
    }

    override fun getBackdropImages(detailContent: SeasonDetail) = detailContent.images?.backdrops

    override fun getPosterImages(detailContent: SeasonDetail) = detailContent.images?.posters

    override fun getVideos(detailContent: SeasonDetail) = detailContent.videos

    override fun getCredits(detailContent: SeasonDetail) = detailContent.credits

    override fun getErrorMessageId() = R.string.error_load_season_detail
}