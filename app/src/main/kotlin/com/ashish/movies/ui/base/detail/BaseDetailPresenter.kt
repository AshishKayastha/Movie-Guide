package com.ashish.movies.ui.base.detail

import com.ashish.movies.R
import com.ashish.movies.data.models.CreditResults
import com.ashish.movies.ui.base.mvp.RxPresenter
import com.ashish.movies.utils.Utils
import io.reactivex.disposables.Disposable
import timber.log.Timber
import java.io.IOException

/**
 * Created by Ashish on Jan 03.
 */
abstract class BaseDetailPresenter<in I, V : BaseDetailMvpView<I>> : RxPresenter<V>() {

    open fun loadDetailContent(id: Long?) {
        if (Utils.isOnline()) {
            if (id != null) {
                getView()?.showProgress()
                addDisposable(getDetailContent(id))
            }
        } else {
            getView()?.apply {
                showToastMessage(R.string.error_no_internet)
                finishActivity()
            }
        }
    }

    abstract fun getDetailContent(id: Long): Disposable

    protected fun showCredits(creditResults: CreditResults?) {
        getView()?.apply {
            showItemList(creditResults?.cast) { showCastList(it) }
            showItemList(creditResults?.crew) { showCrewList(it) }
        }
    }

    protected fun onLoadDetailError(t: Throwable, messageId: Int) {
        Timber.e(t)
        getView()?.apply {
            showErrorToast(t, messageId)
            finishActivity()
        }
    }

    protected fun showErrorToast(t: Throwable, messageId: Int) {
        getView()?.apply {
            if (t is IOException) {
                showToastMessage(R.string.error_no_internet)
            } else {
                showToastMessage(messageId)
            }
        }
    }

    protected fun <T> showItemList(itemList: List<T>?, showData: (List<T>) -> Unit) {
        if (itemList != null && itemList.isNotEmpty()) showData(itemList)
    }
}