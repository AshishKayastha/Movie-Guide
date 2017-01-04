package com.ashish.movies.ui.base.detail

import com.ashish.movies.ui.base.mvp.RxPresenter
import com.ashish.movies.utils.Utils
import io.reactivex.disposables.Disposable

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
        }
    }

    abstract fun getDetailContent(id: Long): Disposable

    protected fun <T> showItemList(itemList: List<T>?, showData: (List<T>) -> Unit) {
        if (itemList != null && itemList.isNotEmpty()) showData(itemList)
    }
}