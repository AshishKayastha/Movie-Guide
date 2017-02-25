package com.ashish.movies.ui.tvshow.detail

import com.ashish.movies.R
import com.ashish.movies.data.interactors.TVShowInteractor
import com.ashish.movies.data.models.FullDetailContent
import com.ashish.movies.data.models.TVShowDetail
import com.ashish.movies.ui.base.detail.fulldetail.FullDetailContentPresenter
import com.ashish.movies.utils.extensions.convertListToCommaSeparatedText
import com.ashish.movies.utils.extensions.getFormattedMediumDate
import com.ashish.movies.utils.schedulers.BaseSchedulerProvider
import java.util.*
import javax.inject.Inject

/**
 * Created by Ashish on Jan 03.
 */
class TVShowDetailPresenter @Inject constructor(
        private val tvShowInteractor: TVShowInteractor,
        schedulerProvider: BaseSchedulerProvider
) : FullDetailContentPresenter<TVShowDetail, TVShowDetailView>(schedulerProvider) {

    override fun getDetailContent(id: Long) = tvShowInteractor.getFullTVShowDetail(id)

    override fun showDetailContent(fullDetailContent: FullDetailContent<TVShowDetail>) {
        super.showDetailContent(fullDetailContent)
        getView()?.apply {
            hideProgress()
            val tvShowDetail = fullDetailContent.detailContent
            setTMDbRating(tvShowDetail?.voteAverage)
            showItemList(tvShowDetail?.seasons) { showSeasonsList(it) }
            showItemList(tvShowDetail?.similarTVShowResults?.results) { showSimilarTVShowList(it) }
        }
    }

    override fun getContentList(fullDetailContent: FullDetailContent<TVShowDetail>): List<String> {
        val contentList = ArrayList<String>()
        fullDetailContent.detailContent?.apply {
            val omdbDetail = fullDetailContent.omdbDetail
            contentList.apply {
                add(overview ?: "")
                add(genres.convertListToCommaSeparatedText { it.name.toString() })
                add(omdbDetail?.Rated ?: "")
                add(status ?: "")
                add(omdbDetail?.Awards ?: "")
                add(omdbDetail?.Production ?: "")
                add(omdbDetail?.Country ?: "")
                add(omdbDetail?.Language ?: "")
                add(firstAirDate.getFormattedMediumDate())
                add(lastAirDate.getFormattedMediumDate())
                add(numberOfSeasons.toString())
                add(numberOfEpisodes.toString())
                add(networks.convertListToCommaSeparatedText { it.name.toString() })
            }
        }

        return contentList
    }

    override fun getBackdropImages(detailContent: TVShowDetail) = detailContent.images?.backdrops

    override fun getPosterImages(detailContent: TVShowDetail) = detailContent.images?.posters

    override fun getVideos(detailContent: TVShowDetail) = detailContent.videos

    override fun getCredits(detailContent: TVShowDetail) = detailContent.creditsResults

    override fun getErrorMessageId() = R.string.error_load_tv_detail
}