package com.ashish.movieguide.ui.base.recyclerview

import com.ashish.movieguide.R
import com.ashish.movieguide.data.network.entities.tmdb.Results
import com.ashish.movieguide.ui.base.mvp.RxPresenter
import com.ashish.movieguide.ui.common.adapter.RecyclerViewItem
import com.ashish.movieguide.utils.AuthException
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import timber.log.Timber
import java.io.IOException
import java.util.ArrayList

abstract class RecyclerViewPresenter<I : RecyclerViewItem, V : RecyclerViewMvpView<I>>(
        schedulerProvider: BaseSchedulerProvider
) : RxPresenter<V>(schedulerProvider) {

    private var totalPages = 1
    private var currentPage: Int = 1
    private var itemList: ArrayList<I>? = null

    fun loadData(type: Int?) {
        if (itemList.isNotNullOrEmpty()) {
            showItemList()
        } else {
            fetchFreshData(type)
        }
    }

    fun fetchFreshData(type: Int?, showProgress: Boolean = true) {
        addDisposable(getResults(getType(type), 1)
                .doOnSuccess { totalPages = it.totalPages }
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { if (showProgress) view?.setLoadingIndicator(true) }
                .doFinally { view?.setLoadingIndicator(false) }
                .subscribe(::showResults, ::showErrorMessage))
    }

    abstract fun getResults(type: String?, page: Int): Single<Results<I>>

    protected open fun getType(type: Int?): String? = null

    private fun showResults(data: Results<I>) {
        currentPage = data.page
        itemList = ArrayList(data.results)
        showItemList()
    }

    private fun showItemList() {
        view?.run {
            showItemList(itemList)
            setCurrentPage(currentPage)
        }
    }

    fun loadMoreData(type: Int?, page: Int) {
        if (page <= totalPages) {
            addDisposable(getResults(getType(type), page)
                    .observeOn(schedulerProvider.ui())
                    .doOnSubscribe { view?.showLoadingItem() }
                    .subscribe(::addNewItemList, ::handleLoadMoreError))
        }
    }

    private fun addNewItemList(data: Results<I>?) {
        view?.run {
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
        showErrorMessage(t)
        view?.run {
            removeLoadingItem()
            resetLoading()
            showErrorView()
        }
    }

    private fun showErrorMessage(t: Throwable) {
        Timber.e(t)
        view?.run {
            when (t) {
                is IOException -> showMessage(R.string.error_no_internet)
                is AuthException -> showMessage(R.string.error_not_logged_in)
                else -> showMessage(R.string.error_load_data)
            }
        }
    }
}