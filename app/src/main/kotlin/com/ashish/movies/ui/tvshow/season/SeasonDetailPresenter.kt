package com.ashish.movies.ui.tvshow.season

import com.ashish.movies.R
import com.ashish.movies.data.interactors.TVShowInteractor
import com.ashish.movies.data.models.FullDetailContent
import com.ashish.movies.data.models.SeasonDetail
import com.ashish.movies.ui.base.detail.fulldetail.FullDetailContentPresenter
import com.ashish.movies.utils.extensions.getFormattedMediumDate
import java.util.*
import javax.inject.Inject

/**
 * Created by Ashish on Jan 07.
 */
class SeasonDetailPresenter @Inject constructor(private val tvShowInteractor: TVShowInteractor)
    : FullDetailContentPresenter<SeasonDetail, SeasonDetailView>() {

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
            contentList.apply {
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