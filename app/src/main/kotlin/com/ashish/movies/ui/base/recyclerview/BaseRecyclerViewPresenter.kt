package com.ashish.movies.ui.base.recyclerview

import com.ashish.movies.data.models.Results
import com.ashish.movies.ui.base.mvp.RxPresenter
import com.ashish.movies.ui.common.adapter.ViewType
import rx.Observable
import timber.log.Timber

/**
 * Created by Ashish on Dec 31.
 */
abstract class BaseRecyclerViewPresenter<I : ViewType, V : BaseRecyclerViewMvpView<I>> : RxPresenter<V>() {

    protected var totalPages = 1

    abstract fun loadData(type: Int?, page: Int = 1, showProgress: Boolean = true)

    protected fun getDataByType(type: String?, page: Int, showProgress: Boolean) {
        if (showProgress) getView()?.showProgress()
        addSubscription(getData(type, page)
                .doOnNext { results -> totalPages = results.totalPages }
                .subscribe({ results -> showData(results) }, { t -> handleError(t) }))
    }

    abstract fun getData(type: String?, page: Int): Observable<Results<I>>

    protected fun showData(data: Results<I>?) {
        getView()?.apply {
            showItemList(data?.results)
            hideProgress()
        }
    }

    abstract fun loadMoreData(type: Int?, page: Int)

    protected fun getMoreDataByType(type: String?, page: Int) {
        addSubscription(getData(type, page)
                .subscribe({ results -> addNewItems(results) }, { t -> handleError(t) }))
    }

    protected fun addNewItems(data: Results<I>?) {
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