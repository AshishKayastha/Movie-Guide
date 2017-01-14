package com.ashish.movies.ui.tvshow.detail

import com.ashish.movies.R
import com.ashish.movies.data.interactors.TVShowInteractor
import com.ashish.movies.data.models.FullDetailContent
import com.ashish.movies.data.models.TVShowDetail
import com.ashish.movies.ui.base.detail.BaseDetailPresenter
import com.ashish.movies.utils.extensions.convertListToCommaSeparatedText
import com.ashish.movies.utils.extensions.getFormattedMediumDate
import javax.inject.Inject

/**
 * Created by Ashish on Jan 03.
 */
class TVShowDetailPresenter @Inject constructor(private val tvShowInteractor: TVShowInteractor)
    : BaseDetailPresenter<TVShowDetail, TVShowDetailView>() {

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

    override fun addContents(fullDetailContent: FullDetailContent<TVShowDetail>) {
        fullDetailContent.detailContent?.apply {
            val omdbDetail = fullDetailContent.omdbDetail
            contentList.add(overview ?: "")
            contentList.add(genres.convertListToCommaSeparatedText { it.name.toString() })
            contentList.add(omdbDetail?.Rated ?: "")
            contentList.add(status ?: "")
            contentList.add(omdbDetail?.Awards ?: "")
            contentList.add(omdbDetail?.Production ?: "")
            contentList.add(omdbDetail?.Country ?: "")
            contentList.add(omdbDetail?.Language ?: "")
            contentList.add(firstAirDate.getFormattedMediumDate())
            contentList.add(lastAirDate.getFormattedMediumDate())
            contentList.add(numberOfSeasons.toString())
            contentList.add(numberOfEpisodes.toString())
            contentList.add(networks.convertListToCommaSeparatedText { it.name.toString() })
        }
    }

    override fun getBackdropImages(detailContent: TVShowDetail) = detailContent.images?.backdrops

    override fun getPosterImages(detailContent: TVShowDetail) = detailContent.images?.posters

    override fun getCredits(detailContent: TVShowDetail) = detailContent.creditsResults

    override fun getErrorMessageId() = R.string.error_load_tv_detail
}