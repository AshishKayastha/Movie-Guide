package com.ashish.movies.ui.base.recyclerview

import com.ashish.movies.R
import com.ashish.movies.data.models.Results
import com.ashish.movies.ui.base.mvp.RxPresenter
import com.ashish.movies.ui.common.adapter.ViewType
import com.ashish.movies.utils.Utils
import io.reactivex.Observable
import timber.log.Timber
import java.io.IOException

/**
 * Created by Ashish on Dec 31.
 */
abstract class BaseRecyclerViewPresenter<I : ViewType, V : BaseRecyclerViewMvpView<I>> : RxPresenter<V>() {

    private var totalPages = 1

    fun loadData(type: Int?, page: Int = 1, showProgress: Boolean = true) {
        if (Utils.isOnline()) {
            if (showProgress) getView()?.showProgress()
            addDisposable(getResultsObservable(getType(type), page)
                    .doOnNext { totalPages = it.totalPages }
                    .subscribe({ showItemList(it) }, { handleError(it) }))
        } else {
            getView()?.showMessage(R.string.error_no_internet)
        }
    }

    protected open fun getType(type: Int?): String? = null

    protected abstract fun getResultsObservable(type: String?, page: Int): Observable<Results<I>>

    protected fun showItemList(data: Results<I>?) {
        getView()?.apply {
            showItemList(data?.results)
            hideProgress()
        }
    }

    protected fun handleError(t: Throwable) {
        Timber.e(t)
        getView()?.apply {
            hideProgress()
            showErrorMessage(t)
        }
    }

    fun loadMoreData(type: Int?, page: Int) {
        if (Utils.isOnline()) {
            if (page <= totalPages) {
                getView()?.showLoadingItem()
                addDisposable(getResultsObservable(getType(type), page)
                        .subscribe({ addNewItemList(it) }, { handleLoadMoreError(it) }))
            }
        } else {
            getView()?.apply {
                resetLoading()
                showMessage(R.string.error_no_internet)
            }
        }
    }

    protected fun addNewItemList(data: Results<I>?) {
        getView()?.apply {
            if (data != null) {
                addNewItemList(data.results)
            } else {
                removeLoadingItem()
            }
        }
    }

    protected fun handleLoadMoreError(t: Throwable) {
        Timber.e(t)
        getView()?.apply {
            removeLoadingItem()
            resetLoading()
            showErrorMessage(t)
        }
    }

    protected fun showErrorMessage(t: Throwable) {
        getView()?.apply {
            if (t is IOException) {
                showMessage(R.string.error_no_internet)
            } else {
                showMessage(R.string.error_load_data)
            }
        }
    }
}