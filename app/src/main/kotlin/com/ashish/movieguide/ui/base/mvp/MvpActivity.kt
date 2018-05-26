package com.ashish.movieguide.ui.base.mvp

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import com.ashish.movieguide.R
import com.ashish.movieguide.utils.extensions.bindOptionalView
import com.ashish.movieguide.utils.extensions.getExtrasOrRestore
import com.ashish.movieguide.utils.extensions.performAction
import com.ashish.movieguide.utils.extensions.showSnackBar
import com.ashish.movieguide.utils.extensions.showToast
import com.evernote.android.state.StateSaver
import dagger.android.AndroidInjection
import net.grandcentrix.thirtyinch.TiActivity

abstract class MvpActivity<V : MvpView, P : RxPresenter<V>> : TiActivity<P, V>(), MvpView {

    protected val toolbar: Toolbar? by bindOptionalView(R.id.toolbar)
    private val rootView: View? by bindOptionalView(R.id.content_layout)

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        setSupportActionBar(toolbar)

        savedInstanceState.getExtrasOrRestore(this) {
            getIntentExtras(intent.extras)
        }
    }

    abstract fun getLayoutId(): Int

    protected open fun getIntentExtras(extras: Bundle?) {}

    override fun showToastMessage(messageId: Int) = showToast(messageId)

    override fun showMessage(messageId: Int) {
        rootView?.showSnackBar(messageId)
    }

    @SuppressLint("NonMatchingStateSaverCalls")
    override fun onSaveInstanceState(outState: Bundle) {
        StateSaver.saveInstanceState(this, outState)
        super.onSaveInstanceState(outState)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> performAction { finishAfterTransition() }
        else -> super.onOptionsItemSelected(item)
    }
}