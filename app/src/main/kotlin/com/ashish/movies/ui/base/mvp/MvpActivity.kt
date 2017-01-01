package com.ashish.movies.ui.base.mvp

import android.os.Bundle
import android.view.View
import butterknife.bindOptionalView
import com.ashish.movies.R
import com.ashish.movies.ui.base.common.BaseActivity
import com.ashish.movies.utils.extensions.showSnackBar
import javax.inject.Inject

/**
 * Created by Ashish on Dec 31.
 */
abstract class MvpActivity<V : MvpView, P : RxPresenter<V>> : BaseActivity(), MvpView {

    val rootView: View? by bindOptionalView(R.id.content_layout)

    @Inject lateinit var presenter: P

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this as V)
    }

    override fun showMessage(messageId: Int) {
        rootView?.showSnackBar(messageId)
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }
}