package com.ashish.movies.ui.movie.detail

import com.ashish.movies.data.interactors.MovieInteractor
import com.ashish.movies.data.models.MovieDetail
import com.ashish.movies.ui.base.detail.BaseDetailPresenter
import io.reactivex.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashish on Dec 31.
 */
class MovieDetailPresenter @Inject constructor(val movieInteractor: MovieInteractor)
    : BaseDetailPresenter<MovieDetail, MovieDetailMvpView>() {

    override fun getDetailContent(id: Long): Disposable {
        return movieInteractor.getMovieDetailWithCreditsAndSimilarMovies(id)
                .subscribe({ showMovieDetailContents(it) }, { Timber.e(it) })
    }

    private fun showMovieDetailContents(movieDetail: MovieDetail?) {
        getView()?.apply {
            hideProgress()
            showDetailContent(movieDetail)

            val creditResults = movieDetail?.creditsResults
            showItemList(creditResults?.cast) { showCastList(it) }
            showItemList(creditResults?.crew) { showCrewList(it) }

            showItemList(movieDetail?.similarMovieResults?.results) { showSimilarMoviesList(it) }
        }
    }
}