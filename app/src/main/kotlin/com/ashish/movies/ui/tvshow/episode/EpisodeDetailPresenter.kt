package com.ashish.movies.ui.tvshow.episode

import com.ashish.movies.R
import com.ashish.movies.data.interactors.TVShowInteractor
import com.ashish.movies.data.models.EpisodeDetail
import com.ashish.movies.ui.base.detail.BaseDetailMvpView
import com.ashish.movies.ui.base.detail.BaseDetailPresenter
import javax.inject.Inject

/**
 * Created by Ashish on Jan 08.
 */
class EpisodeDetailPresenter @Inject constructor(val tvShowInteractor: TVShowInteractor)
    : BaseDetailPresenter<EpisodeDetail, BaseDetailMvpView<EpisodeDetail>>() {

    private var seasonNumber: Int = 1
    private var episodeNumber: Int = 1

    fun setSeasonAndEpisodeNumber(seasonNumber: Int, episodeNumber: Int) {
        this.seasonNumber = seasonNumber
        this.episodeNumber = episodeNumber
    }

    override fun getDetailContent(id: Long) = tvShowInteractor.getFullEpisodeDetail(id, seasonNumber, episodeNumber)

    override fun getCredits(detailContent: EpisodeDetail?) = detailContent?.credits

    override fun getErrorMessageId() = R.string.error_load_episode_detail
}