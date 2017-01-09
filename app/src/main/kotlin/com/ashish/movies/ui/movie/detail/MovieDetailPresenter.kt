package com.ashish.movies.ui.movie.detail

import com.ashish.movies.R
import com.ashish.movies.data.interactors.MovieInteractor
import com.ashish.movies.data.models.FullDetailContent
import com.ashish.movies.data.models.MovieDetail
import com.ashish.movies.ui.base.detail.BaseDetailPresenter
import javax.inject.Inject

/**
 * Created by Ashish on Dec 31.
 */
class MovieDetailPresenter @Inject constructor(val movieInteractor: MovieInteractor)
    : BaseDetailPresenter<MovieDetail, MovieDetailMvpView>() {

    override fun getDetailContent(id: Long) = movieInteractor.getFullMovieDetail(id)

    override fun showDetailContent(fullDetailContent: FullDetailContent<MovieDetail>) {
        super.showDetailContent(fullDetailContent)
        getView()?.apply {
            hideProgress()
            val movieDetail = fullDetailContent.detailContent
            showItemList(movieDetail?.similarMovieResults?.results) { showSimilarMoviesList(it) }
        }
    }

    override fun getCredits(detailContent: MovieDetail?) = detailContent?.creditsResults

    override fun getErrorMessageId() = R.string.error_load_movie_detail
}