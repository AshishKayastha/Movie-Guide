package com.ashish.movies.ui.base.mvp

import android.content.Context
import android.support.v4.content.Loader
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by Ashish on Jan 12.
 */
class PresenterLoader<P : RxPresenter<*>> @Inject constructor(context: Context,
                                                              private val presenterProvider: Provider<P>)
    : Loader<P>(context) {

    private var presenter: P? = null

    override fun onStartLoading() {
        super.onStartLoading()
        if (presenter != null) {
            deliverResult(presenter)
        } else {
            forceLoad()
        }
    }

    override fun onForceLoad() {
        super.onForceLoad()
        presenter = presenterProvider.get()
        deliverResult(presenter)
    }

    override fun onReset() {
        super.onReset()
        presenter?.onDestroy()
    }
}