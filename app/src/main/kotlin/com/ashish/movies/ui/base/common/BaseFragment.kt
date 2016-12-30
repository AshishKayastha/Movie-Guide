package com.ashish.movies.ui.base.common

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ashish.movies.app.MoviesApp
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.ui.base.mvp.MvpView
import com.ashish.movies.ui.base.mvp.RxPresenter
import com.ashish.movies.utils.extensions.inflate
import javax.inject.Inject

/**
 * Created by Ashish on Dec 26.
 */
abstract class BaseFragment<V : MvpView, P : RxPresenter<V>> : Fragment(), MvpView {

    @Inject lateinit var presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies(MoviesApp.getAppComponent(activity))
        retainInstance = true
    }

    abstract fun injectDependencies(appComponent: AppComponent)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(getLayoutId())
    }

    abstract fun getLayoutId(): Int

    @Suppress("UNCHECKED_CAST")
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this as V)
    }

    override fun showMessage(messageId: Int) {

    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }
}