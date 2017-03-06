package com.ashish.movieguide.ui.movie.detail

import com.ashish.movieguide.R
import com.ashish.movieguide.data.interactors.AuthInteractor
import com.ashish.movieguide.data.interactors.MovieInteractor
import com.ashish.movieguide.data.models.Favorite
import com.ashish.movieguide.data.models.FullDetailContent
import com.ashish.movieguide.data.models.MovieDetail
import com.ashish.movieguide.data.preferences.PreferenceHelper
import com.ashish.movieguide.di.scopes.ActivityScope
import com.ashish.movieguide.ui.base.detail.fulldetail.FullDetailContentPresenter
import com.ashish.movieguide.utils.AuthException
import com.ashish.movieguide.utils.Constants.MEDIA_TYPE_MOVIE
import com.ashish.movieguide.utils.Logger
import com.ashish.movieguide.utils.extensions.convertListToCommaSeparatedText
import com.ashish.movieguide.utils.extensions.getFormattedMediumDate
import com.ashish.movieguide.utils.extensions.getFormattedNumber
import com.ashish.movieguide.utils.extensions.getFormattedRuntime
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by Ashish on Dec 31.
 */
@ActivityScope
class MovieDetailPresenter @Inject constructor(
        private val movieInteractor: MovieInteractor,
        private val authInteractor: AuthInteractor,
        private val preferenceHelper: PreferenceHelper,
        schedulerProvider: BaseSchedulerProvider
) : FullDetailContentPresenter<MovieDetail, MovieDetailView>(schedulerProvider) {

    private var isFavorite: Boolean = false

    override fun getDetailContent(id: Long) = movieInteractor.getFullMovieDetail(id)

    override fun showDetailContent(fullDetailContent: FullDetailContent<MovieDetail>) {
        super.showDetailContent(fullDetailContent)
        getView()?.apply {
            hideProgress()
            val movieDetail = fullDetailContent.detailContent

            // Show favorite menu item only if user is logged in
            if (preferenceHelper.getId() > 0) {
                isFavorite = movieDetail?.movieRatings?.favorite ?: false
                setFavoriteIcon(isFavorite)
            }

            setTMDbRating(movieDetail?.voteAverage)
            showItemList(movieDetail?.similarMovieResults?.results) { showSimilarMoviesList(it) }
        }
    }

    override fun getContentList(fullDetailContent: FullDetailContent<MovieDetail>): List<String> {
        val contentList = ArrayList<String>()
        fullDetailContent.detailContent?.apply {
            val omdbDetail = fullDetailContent.omdbDetail
            contentList.apply {
                add(overview ?: "")
                add(tagline ?: "")
                add(genres.convertListToCommaSeparatedText { it.name.toString() })
                add(omdbDetail?.Rated ?: "")
                add(status ?: "")
                add(omdbDetail?.Awards ?: "")
                add(omdbDetail?.Production ?: "")
                add(omdbDetail?.Country ?: "")
                add(omdbDetail?.Language ?: "")
                add(releaseDate.getFormattedMediumDate())
                add(runtime.getFormattedRuntime())
                add(budget.getFormattedNumber())
                add(revenue.getFormattedNumber())
            }
        }

        return contentList
    }

    override fun getBackdropImages(detailContent: MovieDetail) = detailContent.images?.backdrops

    override fun getPosterImages(detailContent: MovieDetail) = detailContent.images?.posters

    override fun getVideos(detailContent: MovieDetail) = detailContent.videos

    override fun getCredits(detailContent: MovieDetail) = detailContent.creditsResults

    override fun getErrorMessageId() = R.string.error_load_movie_detail

    fun markAsFavorite() {
        val movieId = fullDetailContent?.detailContent?.id
        if (movieId != null) {
            isFavorite = !isFavorite
            getView()?.animateFavoriteIcon(isFavorite)

            val favorite = Favorite(isFavorite, movieId, MEDIA_TYPE_MOVIE)
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