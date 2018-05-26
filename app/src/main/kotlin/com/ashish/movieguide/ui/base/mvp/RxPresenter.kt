package com.ashish.movieguide.ui.base.mvp

import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import net.grandcentrix.thirtyinch.TiPresenter

/**
 * This is a base presenter class which handles attaching/detaching presenter from view and
 * will also clear any network request when the presenter is completely destroyed.
 */
abstract class RxPresenter<V : MvpView>(
        protected val schedulerProvider: BaseSchedulerProvider
) : TiPresenter<V>() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    protected fun addDisposable(disposable: Disposable) = compositeDisposable.add(disposable)

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}