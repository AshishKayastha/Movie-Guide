package com.ashish.movies.ui.base.mvp

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

/**
 * Created by Ashish on Dec 28.
 */
abstract class RxPresenter<V : MvpView> {

    private var viewWeakRef: WeakReference<V>? = null
    private lateinit var compositeDisposable: CompositeDisposable

    open fun attachView(view: V) {
        viewWeakRef = WeakReference(view)
        compositeDisposable = CompositeDisposable()
    }

    protected fun getView() = viewWeakRef?.get()

    protected fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    open fun detachView() {
        compositeDisposable.clear()
        viewWeakRef?.clear()
        viewWeakRef = null
    }
}