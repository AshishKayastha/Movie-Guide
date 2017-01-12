package com.ashish.movies.ui.movie.detail

import com.ashish.movies.R
import com.ashish.movies.data.interactors.MovieInteractor
import com.ashish.movies.data.models.FullDetailContent
import com.ashish.movies.data.models.MovieDetail
import com.ashish.movies.ui.base.detail.BaseDetailPresenter
import com.ashish.movies.utils.extensions.convertListToCommaSeparatedText
import com.ashish.movies.utils.extensions.getFormattedMediumDate
import com.ashish.movies.utils.extensions.getFormattedNumber
import com.ashish.movies.utils.extensions.getFormattedRuntime
import javax.inject.Inject

/**
 * Created by Ashish on Dec 31.
 */
class MovieDetailPresenter @Inject constructor(private val movieInteractor: MovieInteractor)
    : BaseDetailPresenter<MovieDetail, MovieDetailView>() {

    override fun getDetailContent(id: Long) = movieInteractor.getFullMovieDetail(id)

    override fun showDetailContent(fullDetailContent: FullDetailContent<MovieDetail>) {
        super.showDetailContent(fullDetailContent)
        getView()?.apply {
            hideProgress()
            val movieDetail = fullDetailContent.detailContent
            showItemList(movieDetail?.similarMovieResults?.results) { showSimilarMoviesList(it) }
        }
    }

    override fun addContents(fullDetailContent: FullDetailContent<MovieDetail>) {
        fullDetailContent.detailContent?.apply {
            val omdbDetail = fullDetailContent.omdbDetail
            contentList.add(overview ?: "")
            contentList.add(tagline ?: "")
            contentList.add(genres.convertListToCommaSeparatedText { it.name.toString() })
            contentList.add(omdbDetail?.Rated ?: "")
            contentList.add(status ?: "")
            contentList.add(omdbDetail?.Awards ?: "")
            contentList.add(omdbDetail?.Production ?: "")
            contentList.add(omdbDetail?.Country ?: "")
            contentList.add(omdbDetail?.Language ?: "")
            contentList.add(releaseDate.getFormattedMediumDate())
            contentList.add(runtime.getFormattedRuntime())
            contentList.add(budget.getFormattedNumber())
            contentList.add(revenue.getFormattedNumber())
        }
    }

    override fun getCredits(detailContent: MovieDetail?) = detailContent?.creditsResults

    override fun getErrorMessageId() = R.string.error_load_movie_detail
}