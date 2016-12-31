package com.ashish.movies.ui.tvshow

import com.ashish.movies.data.api.TVShowService.Companion.AIRING_TODAY
import com.ashish.movies.data.api.TVShowService.Companion.ON_THE_AIR
import com.ashish.movies.data.api.TVShowService.Companion.POPULAR
import com.ashish.movies.data.api.TVShowService.Companion.TOP_RATED
import com.ashish.movies.data.interactors.TVShowInteractor
import com.ashish.movies.data.models.TVShowResults
import com.ashish.movies.ui.base.mvp.RxPresenter
import com.ashish.movies.ui.tvshow.TVShowFragment.Companion.AIRING_TODAY_TV_SHOWS
import com.ashish.movies.ui.tvshow.TVShowFragment.Companion.POPULAR_TV_SHOWS
import com.ashish.movies.ui.tvshow.TVShowFragment.Companion.TOP_RATED_TV_SHOWS
import com.ashish.movies.utils.Utils
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashish on Dec 30.
 */
class TVShowPresenter @Inject constructor(val tvShowInteractor: TVShowInteractor) : RxPresenter<TVShowMvpView>() {

    private var totalPages = 1

    fun getTVShowList(tvShowType: Int?, page: Int = 1, showProgress: Boolean = true) {
        if (Utils.isOnline()) {
            when (tvShowType) {
                POPULAR_TV_SHOWS -> getTVShowsByType(POPULAR, page, showProgress)
                TOP_RATED_TV_SHOWS -> getTVShowsByType(TOP_RATED, page, showProgress)
                AIRING_TODAY_TV_SHOWS -> getTVShowsByType(AIRING_TODAY, page, showProgress)
                else -> getTVShowsByType(ON_THE_AIR, page, showProgress)
            }
        }
    }

    private fun getTVShowsByType(tvShowType: String, page: Int, showProgress: Boolean) {
        if (showProgress) getView()?.showProgress()
        addSubscription(tvShowInteractor.getTVShowsByType(tvShowType, page)
                .doOnNext { tvShowResults -> totalPages = tvShowResults.totalPages }
                .subscribe({ tvShowResults -> showTVShowList(tvShowResults) }, { t -> handleGetTVShowError(t) }))
    }

    private fun showTVShowList(tvShowResults: TVShowResults) {
        getView()?.apply {
            hideProgress()
            showItemList(tvShowResults.results)
        }
    }

    fun loadMoreTVShows(tvShowType: Int?, page: Int) {
        if (Utils.isOnline()) {
            if (page <= totalPages) {
                when (tvShowType) {
                    POPULAR_TV_SHOWS -> getMoreTVShowsByType(POPULAR, page)
                    TOP_RATED_TV_SHOWS -> getMoreTVShowsByType(TOP_RATED, page)
                    AIRING_TODAY_TV_SHOWS -> getMoreTVShowsByType(AIRING_TODAY, page)
                    else -> getMoreTVShowsByType(ON_THE_AIR, page)
                }
            }
        }
    }

    private fun getMoreTVShowsByType(movieType: String, page: Int) {
        addSubscription(tvShowInteractor.getTVShowsByType(movieType, page)
                .subscribe({ tvShowResults -> addTVShowItems(tvShowResults) }, { t -> handleGetTVShowError(t) }))
    }

    private fun addTVShowItems(tvShowResults: TVShowResults?) {
        getView()?.apply {
            hideProgress()
            addNewItems(tvShowResults?.results)
        }
    }

    private fun handleGetTVShowError(t: Throwable) {
        Timber.e(t)
        getView()?.apply {
            hideProgress()
        }
    }
}