package com.ashish.movieguide.ui.base.recyclerview

import com.ashish.movieguide.R
import com.ashish.movieguide.data.network.entities.tmdb.Results
import com.ashish.movieguide.ui.base.mvp.RxPresenter
import com.ashish.movieguide.ui.common.adapter.ViewType
import com.ashish.movieguide.utils.AuthException
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import timber.log.Timber
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
        addDisposable(getResults(getType(type), 1)
                .doOnSuccess { totalPages = it.totalPages }
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { if (showProgress) getView()?.showProgress() }
                .doFinally { getView()?.hideProgress() }
                .subscribe({ showResults(it) }, { showErrorMessage(it) }))
    }

    protected open fun getType(type: Int?): String? = null

    protected abstract fun getResults(type: String?, page: Int): Single<Results<I>>

    private fun showResults(data: Results<I>) {
        currentPage = data.page
        itemList = ArrayList(data.results)
        showItemList()
    }

    private fun showItemList() {
        getView()?.run {
            showItemList(itemList)
            setCurrentPage(currentPage)
        }
    }

    fun loadMoreData(type: Int?, page: Int) {
        if (page <= totalPages) {
            addDisposable(getResults(getType(type), page)
                    .observeOn(schedulerProvider.ui())
                    .doOnSubscribe { getView()?.showLoadingItem() }
                    .subscribe({ addNewItemList(it) }, { handleLoadMoreError(it) }))
        }
    }

    private fun addNewItemList(data: Results<I>?) {
        getView()?.run {
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

    private fun handleLoadMoreError(t: Throwable) {
        Timber.e(t)
        getView()?.run {
            removeLoadingItem()
            resetLoading()
            showErrorView()
        }
    }

    private fun showErrorMessage(t: Throwable) {
        Timber.e(t)
        getView()?.run {
            when (t) {
                is IOException -> showNoInternetMessage()
                is AuthException -> showMessage(R.string.error_not_logged_in)
                else -> showMessage(R.string.error_load_data)
            }
        }
    }

    private fun showNoInternetMessage() {
        getView()?.showMessage(R.string.error_no_internet)
    }
}