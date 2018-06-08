package com.ashish.movieguide.ui.base.mvp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ashish.movieguide.R
import com.ashish.movieguide.utils.extensions.getExtrasOrRestore
import com.ashish.movieguide.utils.extensions.inflate
import com.ashish.movieguide.utils.extensions.showSnackBar
import com.ashish.movieguide.utils.extensions.showToast
import com.ashish.movieguide.utils.extensions.watchFragmentLeaks
import com.evernote.android.state.StateSaver
import dagger.android.support.AndroidSupportInjection
import net.grandcentrix.thirtyinch.TiFragment

abstract class MvpFragment<V : MvpView, P : RxPresenter<V>> : TiFragment<P, V>(), MvpView {

    private var rootView: View? = null

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return container?.inflate(getLayoutId())
    }

    abstract fun getLayoutId(): Int

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = activity?.findViewById(R.id.content_layout)

        savedInstanceState.getExtrasOrRestore(this) {
            getFragmentArguments(arguments)
        }
    }

    protected open fun getFragmentArguments(arguments: Bundle?) {}

    @SuppressLint("NonMatchingStateSaverCalls")
    override fun onSaveInstanceState(outState: Bundle) {
        StateSaver.saveInstanceState(this, outState)
        super.onSaveInstanceState(outState)
    }

    override fun showToastMessage(messageId: Int, msgType: Int) {
        activity?.showToast(messageId, msgType)
    }

    override fun showMessage(messageId: Int) {
        rootView?.showSnackBar(messageId)
    }

    override fun onDestroy() {
        super.onDestroy()
        activity.watchFragmentLeaks()
    }
}