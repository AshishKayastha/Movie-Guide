package com.ashish.movies.ui.base.mvp

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ashish.movies.R
import com.ashish.movies.app.MoviesApp
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.utils.extensions.inflate
import com.ashish.movies.utils.extensions.showSnackBar
import com.ashish.movies.utils.extensions.showToast
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by Ashish on Dec 26.
 */
abstract class MvpFragment<V : MvpView, P : RxPresenter<V>> : Fragment(), MvpView, LoaderManager.LoaderCallbacks<P> {

    companion object {
        private const val LOADER_ID = 1002
    }

    @Inject
    lateinit var presenterLoaderProvider: Provider<PresenterLoader<P>>

    protected var presenter: P? = null
    protected var isFirstStart: Boolean = true

    private var rootView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies(MoviesApp.getAppComponent(activity))
    }

    abstract fun injectDependencies(appComponent: AppComponent)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(getLayoutId())
    }

    abstract fun getLayoutId(): Int

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = activity?.findViewById(R.id.content_layout)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loaderManager.initLoader(getLoaderId(), null, this)
    }

    protected open fun getLoaderId() = LOADER_ID

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<P> = presenterLoaderProvider.get()

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
        activity?.showToast(messageId)
    }

    override fun showMessage(messageId: Int) {
        rootView?.showSnackBar(messageId)
    }

    override fun onDestroy() {
        super.onDestroy()
        MoviesApp.getRefWatcher(activity).watch(this)
    }
}