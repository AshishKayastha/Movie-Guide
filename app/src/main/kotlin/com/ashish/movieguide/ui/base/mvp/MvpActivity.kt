package com.ashish.movieguide.ui.base.mvp

import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.view.View
import com.ashish.movieguide.R
import com.ashish.movieguide.ui.base.common.BaseActivity
import com.ashish.movieguide.utils.extensions.bindOptionalView
import com.ashish.movieguide.utils.extensions.showSnackBar
import com.ashish.movieguide.utils.extensions.showToast
import javax.inject.Inject
import javax.inject.Provider

/**
 * Base Activity class that follows MVP pattern. This will handle injecting
 * [PresenterLoader] which is useful for saving and restoring [RxPresenter]
 * state using [LoaderManager].
 */
abstract class MvpActivity<V : MvpView, P : RxPresenter<V>> : BaseActivity(), MvpView,
        LoaderManager.LoaderCallbacks<P> {

    companion object {
        private const val LOADER_ID = 1001
    }

    @Inject
    lateinit var presenterLoaderProvider: Provider<PresenterLoader<P>>

    protected val rootView: View? by bindOptionalView(R.id.content_layout)

    protected var presenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportLoaderManager.initLoader(LOADER_ID, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<P> = presenterLoaderProvider.get()

    override fun onLoadFinished(loader: Loader<P>?, presenter: P) {
        this.presenter = presenter
    }

    override fun onLoaderReset(loader: Loader<P>?) {
        presenter = null
    }

    @Suppress("UNCHECKED_CAST")
    override fun onStart() {
        super.onStart()
        presenter?.attachView(this as V)
    }

    override fun onStop() {
        presenter?.detachView()
        super.onStop()
    }

    override fun showToastMessage(messageId: Int) = showToast(messageId)

    override fun showMessage(messageId: Int) {
        rootView?.showSnackBar(messageId)
    }
}