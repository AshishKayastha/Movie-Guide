package com.ashish.movies.ui.base.recyclerview

import com.ashish.movies.R
import com.ashish.movies.data.models.Results
import com.ashish.movies.ui.base.mvp.RxPresenter
import com.ashish.movies.ui.common.adapter.ViewType
import com.ashish.movies.utils.Logger
import com.ashish.movies.utils.Utils
import com.ashish.movies.utils.extensions.isNotNullOrEmpty
import com.ashish.movies.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import java.io.IOException
import java.util.ArrayList

/**
 * Created by Ashish on Dec 31.
 */
abstract class BaseRecyclerViewPresenter<I : ViewType, V : BaseRecyclerViewMvpView<I>>(
        schedulerProvider: BaseSchedulerProvider
) : RxPresenter<V>(schedulerProvider) {

    private var totalPages = 1
    private var isFirstStart = true
    private var currentPage: Int = 1
    private var itemList: ArrayList<I>? = null

    fun loadData(type: Int?, showProgress: Boolean = true) {
        if (isFirstStart) {
            if (itemList.isNotNullOrEmpty()) {
                showItemList()
            } else {
                loadFreshData(type, showProgress)
            }

            isFirstStart = false
        }
    }

    fun loadFreshData(type: Int?, showProgress: Boolean = true) {
        if (Utils.isOnline()) {
            addDisposable(getResultsObservable(getType(type), 1)
                    .doOnNext { totalPages = it.totalPages }
                    .observeOn(schedulerProvider.ui())
                    .doOnSubscribe { if (showProgress) getView()?.showProgress() }
                    .doFinally { getView()?.hideProgress() }
                    .subscribe({ showResults(it) }, { showErrorMessage(it) }))
        } else {
            getView()?.apply {
                hideProgress()
                showMessage(R.string.error_no_internet)
            }
        }
    }

    protected open fun getType(type: Int?): String? = null

    protected abstract fun getResultsObservable(type: String?, page: Int): Observable<Results<I>>

    protected fun showResults(data: Results<I>) {
        currentPage = data.page
        itemList = ArrayList(data.results)
        showItemList()
    }

    private fun showItemList() {
        getView()?.apply {
            showItemList(itemList)
            setCurrentPage(currentPage)
        }
    }

    fun loadMoreData(type: Int?, page: Int) {
        if (Utils.isOnline()) {
            if (page <= totalPages) {
                getView()?.showLoadingItem()
                addDisposable(getResultsObservable(getType(type), page)
                        .observeOn(schedulerProvider.ui())
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
                currentPage = data.page
                val newItemList = data.results
                newItemList?.let { itemList?.addAll(it) }
                addNewItemList(newItemList)
            } else {
                removeLoadingItem()
            }
        }
    }

    protected fun handleLoadMoreError(t: Throwable) {
        getView()?.apply {
            removeLoadingItem()
            resetLoading()
            showErrorMessage(t)
        }
    }

    protected fun showErrorMessage(t: Throwable) {
        Logger.e(t)
        getView()?.apply {
            if (t is IOException) {
                showMessage(R.string.error_no_internet)
            } else {
                showMessage(R.string.error_load_data)
            }
        }
    }
}