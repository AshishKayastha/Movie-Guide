package com.ashish.movieguide.ui.tvshow.detail

import com.ashish.movieguide.R
import com.ashish.movieguide.data.interactors.AuthInteractor
import com.ashish.movieguide.data.interactors.TVShowInteractor
import com.ashish.movieguide.data.models.Favorite
import com.ashish.movieguide.data.models.FullDetailContent
import com.ashish.movieguide.data.models.TVShowDetail
import com.ashish.movieguide.data.preferences.PreferenceHelper
import com.ashish.movieguide.di.scopes.ActivityScope
import com.ashish.movieguide.ui.base.detail.fulldetail.FullDetailContentPresenter
import com.ashish.movieguide.utils.AuthException
import com.ashish.movieguide.utils.Constants.MEDIA_TYPE_TV
import com.ashish.movieguide.utils.Logger
import com.ashish.movieguide.utils.extensions.convertListToCommaSeparatedText
import com.ashish.movieguide.utils.extensions.getFormattedMediumDate
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by Ashish on Jan 03.
 */
@ActivityScope
class TVShowDetailPresenter @Inject constructor(
        private val tvShowInteractor: TVShowInteractor,
        private val authInteractor: AuthInteractor,
        private val preferenceHelper: PreferenceHelper,
        schedulerProvider: BaseSchedulerProvider
) : FullDetailContentPresenter<TVShowDetail, TVShowDetailView>(schedulerProvider) {

    private var isFavorite: Boolean = false

    override fun getDetailContent(id: Long) = tvShowInteractor.getFullTVShowDetail(id)

    override fun showDetailContent(fullDetailContent: FullDetailContent<TVShowDetail>) {
        super.showDetailContent(fullDetailContent)
        getView()?.apply {
            hideProgress()
            val tvShowDetail = fullDetailContent.detailContent

            // Show favorite menu item only if user is logged in
            if (preferenceHelper.getId() > 0) {
                isFavorite = tvShowDetail?.tvRatings?.favorite ?: false
                setFavoriteIcon(isFavorite)
            }

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

    fun markAsFavorite() {
        val tvId = fullDetailContent?.detailContent?.id
        if (tvId != null) {
            isFavorite = !isFavorite
            getView()?.animateFavoriteIcon(isFavorite)

            val favorite = Favorite(isFavorite, tvId, MEDIA_TYPE_TV)
            addDisposable(authInteractor.markAsFavorite(favorite)
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ onMarkAsFavoriteActionSuccess() },
                            { t -> onMarkAsFavoriteActionError(t) }))
        }
    }

    private fun onMarkAsFavoriteActionSuccess() {
        getView()?.showMessage(R.string.success_mark_favorite)
    }

    private fun onMarkAsFavoriteActionError(t: Throwable) {
        Logger.e(t)
        isFavorite = !isFavorite
        getView()?.apply {
            setFavoriteIcon(isFavorite)
            if (t is AuthException) {
                showMessage(R.string.error_not_logged_in)
            } else {
                showMessage(R.string.error_mark_favorite)
            }
        }
    }
}