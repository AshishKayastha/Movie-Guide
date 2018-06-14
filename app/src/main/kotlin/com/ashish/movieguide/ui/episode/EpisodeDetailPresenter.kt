package com.ashish.movieguide.ui.episode

import com.ashish.movieguide.R
import com.ashish.movieguide.data.remote.entities.common.FullDetailContent
import com.ashish.movieguide.data.remote.entities.tmdb.CreditResults
import com.ashish.movieguide.data.remote.entities.tmdb.EpisodeDetail
import com.ashish.movieguide.data.remote.entities.tmdb.ImageItem
import com.ashish.movieguide.data.remote.entities.tmdb.Videos
import com.ashish.movieguide.data.remote.entities.trakt.SyncEpisode
import com.ashish.movieguide.data.remote.entities.trakt.SyncItems
import com.ashish.movieguide.data.remote.entities.trakt.TraktEpisode
import com.ashish.movieguide.data.remote.interactors.TVShowInteractor
import com.ashish.movieguide.ui.base.detail.fulldetail.FullDetailContentPresenter
import com.ashish.movieguide.ui.common.rating.RatingManager
import com.ashish.movieguide.utils.extensions.getFormattedMediumDate
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import java.util.ArrayList
import java.util.Collections
import javax.inject.Inject

/**
 * Created by Ashish on Jan 08.
 */
class EpisodeDetailPresenter @Inject constructor(
        private val tvShowInteractor: TVShowInteractor,
        private val ratingManager: RatingManager,
        schedulerProvider: BaseSchedulerProvider
) : FullDetailContentPresenter<EpisodeDetail, TraktEpisode, EpisodeDetailView>(schedulerProvider) {

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

    override fun getDetailContent(id: Long): Observable<FullDetailContent<EpisodeDetail, TraktEpisode>> {
        return tvShowInteractor.getFullEpisodeDetail(id, seasonNumber, episodeNumber)
    }

    override fun showDetailContent(fullDetailContent: FullDetailContent<EpisodeDetail, TraktEpisode>) {
        super.showDetailContent(fullDetailContent)
        setTMDbRating(fullDetailContent.detailContent?.voteAverage)
    }

    override fun getContentList(fullDetailContent: FullDetailContent<EpisodeDetail, TraktEpisode>): List<String> {
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

    override fun getBackdropImages(detailContent: EpisodeDetail): List<ImageItem>? = detailContent.images?.backdrops

    override fun getPosterImages(detailContent: EpisodeDetail): List<ImageItem>? = detailContent.images?.posters

    override fun getVideos(detailContent: EpisodeDetail): Videos? = detailContent.videos

    override fun getCredits(detailContent: EpisodeDetail): CreditResults? = detailContent.credits

    override fun getErrorMessageId() = R.string.error_load_episode_detail

    fun addRating(rating: Int) {
        syncRatings(rating) { syncItems ->
            ratingManager.addRating(syncItems, getEpisodeId(), rating)
        }
    }

    fun removeRating() {
        syncRatings(null) { syncItems ->
            ratingManager.removeRating(syncItems, getEpisodeId())
        }
    }

    private fun syncRatings(rating: Int?, syncRatingAction: (SyncItems) -> Unit) {
        val traktItem = fullDetailContent?.traktItem
        if (traktItem != null) {
            val syncEpisode = SyncEpisode(rating, traktItem.ids, "")
            val syncItems = SyncItems(episodes = Collections.singletonList(syncEpisode))
            syncRatingAction.invoke(syncItems)
        }
    }

    private fun getEpisodeId(): Long = fullDetailContent?.detailContent?.id!!

    override fun onDetachView() {
        ratingManager.setView(null)
        super.onDetachView()
    }

    override fun onDestroy() {
        ratingManager.unsubscribe()
        super.onDestroy()
    }
}