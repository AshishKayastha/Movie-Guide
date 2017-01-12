package com.ashish.movies.ui.base.mvp

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.view.View
import butterknife.bindOptionalView
import com.ashish.movies.R
import com.ashish.movies.ui.base.common.BaseActivity
import com.ashish.movies.utils.extensions.showSnackBar
import com.ashish.movies.utils.extensions.showToast
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by Ashish on Dec 31.
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
    protected var isFirstStart: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportLoaderManager.initLoader(getLoaderId(), null, this)
    }

    protected open fun getLoaderId() = LOADER_ID

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<P> {
        return presenterLoaderProvider.get()
    }

    override fun onLoadFinished(loader: Loader<P>?, presenter: P) {
        onPresenterProvided(presenter)
    }

    @CallSuper
    protected open fun onPresenterProvided(presenter: P) {
        this.presenter = presenter
    }

    override fun onLoaderReset(loader: Loader<P>?) {
        onPresenterDestroyed()
    }

    @CallSuper
    protected open fun onPresenterDestroyed() {
        presenter = null
    }

    @Suppress("UNCHECKED_CAST")
    override fun onStart() {
        super.onStart()
        presenter?.attachView(this as V)
    }

    override fun onStop() {
        super.onStop()
        presenter?.detachView()
    }

    override fun showToastMessage(messageId: Int) {
        showToast(messageId)
    }

    override fun showMessage(messageId: Int) {
        rootView?.showSnackBar(messageId)
    }
}