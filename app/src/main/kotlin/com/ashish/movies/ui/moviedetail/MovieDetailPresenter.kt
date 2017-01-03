package com.ashish.movies.ui.moviedetail

import com.ashish.movies.data.interactors.MovieInteractor
import com.ashish.movies.data.models.CreditResults
import com.ashish.movies.data.models.Movie
import com.ashish.movies.data.models.MovieDetail
import com.ashish.movies.data.models.Results
import com.ashish.movies.ui.base.detail.BaseDetailPresenter
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function3
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashish on Dec 31.
 */
class MovieDetailPresenter @Inject constructor(val movieInteractor: MovieInteractor)
    : BaseDetailPresenter<Movie, MovieDetailMvpView>() {

    override fun getDetail(id: Long): Disposable {
        return Observable.combineLatest(
                movieInteractor.getMovieDetail(id),
                movieInteractor.getMovieCredits(id),
                movieInteractor.getSimilarMovies(id),
                Function3<Movie, CreditResults, Results<Movie>, MovieDetail> { movie, creditsResults, similarMovieResults
                    ->
                    MovieDetail(movie, creditsResults, similarMovieResults?.results)
                })
                .subscribe({ showMovieDetailContents(it) }, { Timber.e(it) })
    }

    private fun showMovieDetailContents(movieDetail: MovieDetail) {
        getView()?.apply {
            hideProgress()
            showDetailContent(movieDetail.movie)

            val creditResults = movieDetail.creditResults
            showItemList(creditResults?.cast) { showCastList(it) }
            showItemList(creditResults?.crew) { showCrewList(it) }

            showItemList(movieDetail.similarMovies) { showSimilarContentList(it) }
        }
    }
}