package com.ashish.movies.ui.tvshow.detail

import com.ashish.movies.data.interactors.TVShowInteractor
import com.ashish.movies.data.models.CreditResults
import com.ashish.movies.data.models.DetailResults
import com.ashish.movies.data.models.Results
import com.ashish.movies.data.models.TVShow
import com.ashish.movies.ui.base.detail.BaseDetailMvpView
import com.ashish.movies.ui.base.detail.BaseDetailPresenter
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function3
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashish on Jan 03.
 */
class TVShowDetailPresenter @Inject constructor(val tvShowInteractor: TVShowInteractor)
    : BaseDetailPresenter<TVShow, BaseDetailMvpView<TVShow>>() {

    override fun getDetail(id: Long): Disposable {
        return Observable.combineLatest(
                tvShowInteractor.getTVShowDetail(id),
                tvShowInteractor.getTVShowCredits(id),
                tvShowInteractor.getSimilarTVShows(id),
                Function3<TVShow, CreditResults, Results<TVShow>, DetailResults<TVShow>> { tvShow, creditsResults,
                                                                                           similarTVShowResults
                    ->
                    DetailResults(tvShow, creditsResults, similarTVShowResults?.results)
                })
                .subscribe({ showMovieDetailContents(it) }, { Timber.e(it) })
    }

    private fun showMovieDetailContents(detailResults: DetailResults<TVShow>) {
        getView()?.apply {
            hideProgress()
            showDetailContent(detailResults.item)

            val creditResults = detailResults.creditResults
            showItemList(creditResults?.cast) { showCastList(it) }
            showItemList(creditResults?.crew) { showCrewList(it) }

            showItemList(detailResults.similarItems) { showSimilarContentList(it) }
        }
    }
}