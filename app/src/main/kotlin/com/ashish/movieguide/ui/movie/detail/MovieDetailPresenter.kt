package com.ashish.movieguide.ui.movie.detail

import com.ashish.movieguide.R
import com.ashish.movieguide.data.interactors.MovieInteractor
import com.ashish.movieguide.data.models.FullDetailContent
import com.ashish.movieguide.data.models.MovieDetail
import com.ashish.movieguide.di.scopes.ActivityScope
import com.ashish.movieguide.ui.base.detail.fulldetail.FullDetailContentPresenter
import com.ashish.movieguide.ui.common.personalcontent.PersonalContentManager
import com.ashish.movieguide.utils.Constants.MEDIA_TYPE_MOVIE
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
        private val personalContentManager: PersonalContentManager,
        schedulerProvider: BaseSchedulerProvider
) : FullDetailContentPresenter<MovieDetail, MovieDetailView>(schedulerProvider) {

    override fun attachView(view: MovieDetailView) {
        super.attachView(view)
        personalContentManager.setView(view)
    }

    override fun getDetailContent(id: Long) = movieInteractor.getFullMovieDetail(id)

    override fun showDetailContent(fullDetailContent: FullDetailContent<MovieDetail>) {
        super.showDetailContent(fullDetailContent)
        getView()?.apply {
            hideProgress()
            val movieDetail = fullDetailContent.detailContent
            personalContentManager.setAccountState(movieDetail?.movieRatings)

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
        personalContentManager.markAsFavorite(movieId, MEDIA_TYPE_MOVIE)
    }

    fun addToWatchlist() {
        val movieId = fullDetailContent?.detailContent?.id
        personalContentManager.addToWatchlist(movieId, MEDIA_TYPE_MOVIE)
    }

    override fun detachView() {
        super.detachView()
        personalContentManager.setView(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        personalContentManager.unsubscribe()
    }
}