package com.ashish.movies.ui.movie.detail

import com.ashish.movies.R
import com.ashish.movies.data.interactors.MovieInteractor
import com.ashish.movies.data.models.MovieDetail
import com.ashish.movies.ui.base.detail.BaseDetailPresenter
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * Created by Ashish on Dec 31.
 */
class MovieDetailPresenter @Inject constructor(val movieInteractor: MovieInteractor)
    : BaseDetailPresenter<MovieDetail, MovieDetailMvpView>() {

    override fun getDetailContent(id: Long): Disposable {
        return movieInteractor.getMovieDetailWithCreditsAndSimilarMovies(id)
                .subscribe({ showMovieDetailContents(it) }, { onLoadDetailError(it, R.string.error_load_movie_detail) })
    }

    private fun showMovieDetailContents(movieDetail: MovieDetail?) {
        getView()?.apply {
            hideProgress()
            showDetailContent(movieDetail)
            showCredits(movieDetail?.creditsResults)
            showItemList(movieDetail?.similarMovieResults?.results) { showSimilarMoviesList(it) }
        }
    }
}