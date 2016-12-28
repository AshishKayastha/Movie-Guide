package com.ashish.movies.ui.base.mvp

import rx.Subscription
import rx.subscriptions.CompositeSubscription
import java.lang.ref.WeakReference

/**
 * Created by Ashish on Dec 28.
 */
abstract class RxPresenter<V : MvpView> {

    private var viewWeakRef: WeakReference<V>? = null
    private var compositeSubscription: CompositeSubscription = CompositeSubscription()

    open fun attachView(view: V) {
        viewWeakRef = WeakReference(view)
    }

    protected fun getView(): V? = viewWeakRef?.get()

    protected fun addSubscription(subscription: Subscription) {
        compositeSubscription.add(subscription)
    }

    open fun detachView() {
        compositeSubscription.unsubscribe()
        viewWeakRef?.clear()
        viewWeakRef = null
    }
}