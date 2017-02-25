package com.ashish.movies.ui.base.mvp

import android.support.annotation.CallSuper
import com.ashish.movies.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * This is a base presenter class which handles attaching/detaching presenter from view and
 * will also clear any network request when the presenter is completely destroyed.
 */
abstract class RxPresenter<V : MvpView>(protected val schedulerProvider: BaseSchedulerProvider) {

    private var view: V? = null
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    @CallSuper
    open fun attachView(view: V) {
        this.view = view
    }

    protected fun getView() = view

    protected fun addDisposable(disposable: Disposable) = compositeDisposable.add(disposable)

    @CallSuper
    open fun detachView() {
        view = null
    }

    @CallSuper
    open fun onDestroy() = compositeDisposable.clear()
}