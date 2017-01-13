package com.ashish.movies.ui.base.mvp

import android.support.annotation.CallSuper
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by Ashish on Dec 28.
 */
abstract class RxPresenter<V : MvpView> {

    private var view: V? = null
    private val viewLifecycle = BehaviorSubject.create<Boolean>()
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    @CallSuper
    open fun attachView(view: V) {
        this.view = view
        viewLifecycle.onNext(true)
    }

    protected fun getView() = view

    protected fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    @CallSuper
    open fun detachView() {
        view = null
        viewLifecycle.onNext(false)
    }

    @CallSuper
    open fun onDestroy() {
        compositeDisposable.clear()
    }

    protected fun getViewState(): Observable<Boolean> {
        return viewLifecycle
    }
}