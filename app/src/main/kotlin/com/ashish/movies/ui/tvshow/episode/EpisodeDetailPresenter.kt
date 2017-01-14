package com.ashish.movies.ui.tvshow.episode

import com.ashish.movies.R
import com.ashish.movies.data.interactors.TVShowInteractor
import com.ashish.movies.data.models.EpisodeDetail
import com.ashish.movies.data.models.FullDetailContent
import com.ashish.movies.ui.base.detail.BaseDetailPresenter
import com.ashish.movies.ui.base.detail.BaseDetailView
import com.ashish.movies.utils.extensions.getFormattedMediumDate
import java.util.*
import javax.inject.Inject

/**
 * Created by Ashish on Jan 08.
 */
class EpisodeDetailPresenter @Inject constructor(private val tvShowInteractor: TVShowInteractor)
    : BaseDetailPresenter<EpisodeDetail, BaseDetailView<EpisodeDetail>>() {

    private var seasonNumber: Int = 1
    private var episodeNumber: Int = 1

    fun setSeasonAndEpisodeNumber(seasonNumber: Int, episodeNumber: Int) {
        this.seasonNumber = seasonNumber
        this.episodeNumber = episodeNumber
    }

    override fun getDetailContent(id: Long) = tvShowInteractor.getFullEpisodeDetail(id, seasonNumber, episodeNumber)

    override fun getContentList(fullDetailContent: FullDetailContent<EpisodeDetail>): List<String> {
        val contentList = ArrayList<String>()
        fullDetailContent.detailContent?.apply {
            val omdbDetail = fullDetailContent.omdbDetail
            contentList.add(overview ?: "")
            contentList.add(omdbDetail?.Rated ?: "")
            contentList.add(omdbDetail?.Awards ?: "")
            contentList.add(seasonNumber.toString())
            contentList.add(episodeNumber.toString())
            contentList.add(airDate.getFormattedMediumDate())
            contentList.add(omdbDetail?.Production ?: "")
            contentList.add(omdbDetail?.Country ?: "")
            contentList.add(omdbDetail?.Language ?: "")
        }

        return contentList
    }

    override fun getBackdropImages(detailContent: EpisodeDetail) = detailContent.images?.backdrops

    override fun getPosterImages(detailContent: EpisodeDetail) = detailContent.images?.posters

    override fun getVideos(detailContent: EpisodeDetail) = detailContent.videos

    override fun getCredits(detailContent: EpisodeDetail) = detailContent.credits

    override fun getErrorMessageId() = R.string.error_load_episode_detail
}