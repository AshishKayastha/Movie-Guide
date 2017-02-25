package com.ashish.movies.ui.movie.detail

import com.ashish.movies.R
import com.ashish.movies.data.interactors.MovieInteractor
import com.ashish.movies.data.models.FullDetailContent
import com.ashish.movies.data.models.MovieDetail
import com.ashish.movies.di.scopes.ActivityScope
import com.ashish.movies.ui.base.detail.fulldetail.FullDetailContentPresenter
import com.ashish.movies.utils.extensions.convertListToCommaSeparatedText
import com.ashish.movies.utils.extensions.getFormattedMediumDate
import com.ashish.movies.utils.extensions.getFormattedNumber
import com.ashish.movies.utils.extensions.getFormattedRuntime
import com.ashish.movies.utils.schedulers.BaseSchedulerProvider
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by Ashish on Dec 31.
 */
@ActivityScope
class MovieDetailPresenter @Inject constructor(
        private val movieInteractor: MovieInteractor,
        schedulerProvider: BaseSchedulerProvider
) : FullDetailContentPresenter<MovieDetail, MovieDetailView>(schedulerProvider) {

    override fun getDetailContent(id: Long) = movieInteractor.getFullMovieDetail(id)

    override fun showDetailContent(fullDetailContent: FullDetailContent<MovieDetail>) {
        super.showDetailContent(fullDetailContent)
        getView()?.apply {
            hideProgress()
            val movieDetail = fullDetailContent.detailContent
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
}