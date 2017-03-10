package com.ashish.movieguide.ui.base.recyclerview

import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.Results
import com.ashish.movieguide.ui.base.mvp.RxPresenter
import com.ashish.movieguide.ui.common.adapter.ViewType
import com.ashish.movieguide.utils.AuthException
import com.ashish.movieguide.utils.Logger
import com.ashish.movieguide.utils.Utils
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
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
                    .doOnSuccess { totalPages = it.totalPages }
                    .observeOn(schedulerProvider.ui())
                    .doOnSubscribe { if (showProgress) getView()?.showProgress() }
                    .doFinally { getView()?.hideProgress() }
                    .subscribe({ showResults(it) }, { showErrorMessage(it) }))
        } else {
            getView()?.hideProgress()
            showNoInternetMessage()
        }
    }

    protected open fun getType(type: Int?): String? = null

    protected abstract fun getResultsObservable(type: String?, page: Int): Single<Results<I>>

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
            getView()?.resetLoading()
            showNoInternetMessage()
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
                showNoInternetMessage()
            } else if (t is AuthException) {
                showMessage(R.string.error_not_logged_in)
            } else {
                showMessage(R.string.error_load_data)
            }
        }
    }

    protected fun showNoInternetMessage() {
        getView()?.showMessage(R.string.error_no_internet)
    }
}