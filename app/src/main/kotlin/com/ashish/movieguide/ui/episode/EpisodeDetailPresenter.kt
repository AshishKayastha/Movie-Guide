package com.ashish.movieguide.ui.episode

import com.ashish.movieguide.R
import com.ashish.movieguide.data.interactors.TVShowInteractor
import com.ashish.movieguide.data.models.EpisodeDetail
import com.ashish.movieguide.data.models.FullDetailContent
import com.ashish.movieguide.di.scopes.ActivityScope
import com.ashish.movieguide.ui.base.detail.fulldetail.FullDetailContentPresenter
import com.ashish.movieguide.ui.common.rating.RatingManager
import com.ashish.movieguide.utils.extensions.getFormattedMediumDate
import com.ashish.movieguide.utils.extensions.getRatingValue
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by Ashish on Jan 08.
 */
@ActivityScope
class EpisodeDetailPresenter @Inject constructor(
        private val tvShowInteractor: TVShowInteractor,
        private val ratingManager: RatingManager,
        schedulerProvider: BaseSchedulerProvider
) : FullDetailContentPresenter<EpisodeDetail, EpisodeDetailView>(schedulerProvider) {

    private var tvId: Long = 0L
    private var seasonNumber: Int = 1
    private var episodeNumber: Int = 1

    override fun attachView(view: EpisodeDetailView) {
        super.attachView(view)
        ratingManager.setView(view)
    }

    fun setSeasonAndEpisodeNumber(seasonNumber: Int, episodeNumber: Int) {
        this.seasonNumber = seasonNumber
        this.episodeNumber = episodeNumber
    }

    override fun getDetailContent(id: Long): Observable<FullDetailContent<EpisodeDetail>> {
        tvId = id
        return tvShowInteractor.getFullEpisodeDetail(id, seasonNumber, episodeNumber)
    }

    override fun showDetailContent(fullDetailContent: FullDetailContent<EpisodeDetail>) {
        super.showDetailContent(fullDetailContent)
        setTMDbRating(fullDetailContent.detailContent?.voteAverage)
        getView()?.showSavedRating(fullDetailContent.detailContent?.episodeRatings?.getRatingValue())
    }

    override fun getContentList(fullDetailContent: FullDetailContent<EpisodeDetail>): List<String> {
        val contentList = ArrayList<String>()
        fullDetailContent.detailContent?.apply {
            val omdbDetail = fullDetailContent.omdbDetail
            contentList.apply {
                add(overview ?: "")
                add(omdbDetail?.Rated ?: "")
                add(omdbDetail?.Awards ?: "")
                add(seasonNumber.toString())
                add(episodeNumber.toString())
                add(airDate.getFormattedMediumDate())
                add(omdbDetail?.Production ?: "")
                add(omdbDetail?.Country ?: "")
                add(omdbDetail?.Language ?: "")
            }
        }

        return contentList
    }

    override fun getBackdropImages(detailContent: EpisodeDetail) = detailContent.images?.backdrops

    override fun getPosterImages(detailContent: EpisodeDetail) = detailContent.images?.posters

    override fun getVideos(detailContent: EpisodeDetail) = detailContent.videos

    override fun getCredits(detailContent: EpisodeDetail) = detailContent.credits

    override fun getErrorMessageId() = R.string.error_load_episode_detail

    fun saveRating(rating: Double) {
        ratingManager.saveRating(tvShowInteractor.rateEpisode(tvId, seasonNumber,
                episodeNumber, rating), getEpisodeId(), rating)
    }

    fun deleteRating() {
        ratingManager.deleteRating(tvShowInteractor.deleteEpisodeRating(tvId, seasonNumber, episodeNumber),
                getEpisodeId())
    }

    private fun getEpisodeId() = fullDetailContent?.detailContent?.id!!

    override fun detachView() {
        super.detachView()
        ratingManager.setView(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        ratingManager.unsubscribe()
    }
}