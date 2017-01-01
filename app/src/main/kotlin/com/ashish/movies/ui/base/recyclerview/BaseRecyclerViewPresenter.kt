package com.ashish.movies.ui.base.recyclerview

import com.ashish.movies.R
import com.ashish.movies.data.models.Results
import com.ashish.movies.ui.base.mvp.RxPresenter
import com.ashish.movies.ui.common.adapter.ViewType
import com.ashish.movies.utils.Utils
import rx.Observable
import timber.log.Timber

/**
 * Created by Ashish on Dec 31.
 */
abstract class BaseRecyclerViewPresenter<I : ViewType, V : BaseRecyclerViewMvpView<I>> : RxPresenter<V>() {

    protected var totalPages = 1

    abstract fun loadData(type: Int?, page: Int = 1, showProgress: Boolean = true)

    protected fun getDataByType(type: String?, page: Int, showProgress: Boolean) {
        if (Utils.isOnline()) {
            if (showProgress) getView()?.showProgress()
            addSubscription(getData(type, page)
                    .doOnNext { totalPages = it.totalPages }
                    .subscribe({ showItemList(it) }, { handleError(it) }))
        } else {
            getView()?.showMessage(R.string.error_no_internet)
        }
    }

    abstract fun getData(type: String?, page: Int): Observable<Results<I>>

    protected fun showItemList(data: Results<I>?) {
        getView()?.apply {
            showItemList(data?.results)
            hideProgress()
        }
    }

    abstract fun loadMoreData(type: Int?, page: Int)

    protected fun getMoreDataByType(type: String?, page: Int) {
        if (Utils.isOnline()) {
            addSubscription(getData(type, page)
                    .subscribe({ addNewItemList(it) },
                            {
                                handleError(it)
                                getView()?.resetLoading()
                            }))
        } else {
            getView()?.showMessage(R.string.error_no_internet)
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

    protected fun handleError(t: Throwable) {
        Timber.e(t)
        getView()?.apply {
            hideProgress()
        }
    }
}