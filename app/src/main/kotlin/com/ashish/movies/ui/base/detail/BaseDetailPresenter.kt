package com.ashish.movies.ui.base.detail

import com.ashish.movies.ui.base.mvp.RxPresenter
import com.ashish.movies.ui.common.adapter.ViewType
import com.ashish.movies.utils.Utils
import io.reactivex.disposables.Disposable

/**
 * Created by Ashish on Jan 03.
 */
abstract class BaseDetailPresenter<in I : ViewType, V : BaseDetailMvpView<I>> : RxPresenter<V>() {

    open fun loadDetailContent(id: Long?) {
        if (Utils.isOnline()) {
            if (id != null) {
                getView()?.showProgress()
                addDisposable(getDetail(id))
            }
        }
    }

    abstract fun getDetail(id: Long): Disposable

    protected fun <I : ViewType> showItemList(itemList: List<I>?, showData: (List<I>) -> Unit) {
        if (itemList != null && itemList.isNotEmpty()) showData(itemList)
    }
}