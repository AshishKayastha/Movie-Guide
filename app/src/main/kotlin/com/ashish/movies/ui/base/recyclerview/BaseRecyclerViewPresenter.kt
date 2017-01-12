package com.ashish.movies.ui.base.recyclerview

import com.ashish.movies.R
import com.ashish.movies.data.models.Results
import com.ashish.movies.ui.base.mvp.RxPresenter
import com.ashish.movies.ui.common.adapter.ViewType
import com.ashish.movies.utils.Utils
import com.ashish.movies.utils.extensions.isNotNullOrEmpty
import io.reactivex.Observable
import timber.log.Timber
import java.io.IOException
import java.util.*

/**
 * Created by Ashish on Dec 31.
 */
abstract class BaseRecyclerViewPresenter<I : ViewType, V : BaseRecyclerViewMvpView<I>> : RxPresenter<V>() {

    private var totalPages = 1
    private var itemList: ArrayList<I>? = null

    fun loadData(type: Int?, showProgress: Boolean = true, forceLoad: Boolean = true) {
        if (!forceLoad && itemList.isNotNullOrEmpty()) {
            getView()?.showItemList(itemList)
        } else {
            loadFreshData(showProgress, type)
        }
    }

    private fun loadFreshData(showProgress: Boolean, type: Int?) {
        if (Utils.isOnline()) {
            if (showProgress) getView()?.showProgress()
            addDisposable(getResultsObservable(getType(type), 1)
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
            itemList = ArrayList(data?.results)
            showItemList(itemList)
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
                val newItemList = data.results
                newItemList?.let { itemList?.addAll(it) }
                addNewItemList(newItemList)
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