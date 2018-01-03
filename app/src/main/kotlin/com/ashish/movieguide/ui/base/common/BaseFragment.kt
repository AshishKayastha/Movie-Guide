package com.ashish.movieguide.ui.base.common

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ashish.movieguide.app.MovieGuideApp
import com.ashish.movieguide.utils.extensions.getExtrasOrRestore
import com.ashish.movieguide.utils.extensions.inflate
import icepick.Icepick

/**
 * Created by Ashish on Jan 15.
 */
abstract class BaseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(getLayoutId())
    }

    abstract fun getLayoutId(): Int

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState.getExtrasOrRestore(this) {
            getFragmentArguments(arguments)
        }
    }

    protected open fun getFragmentArguments(arguments: Bundle?) {}

    override fun onSaveInstanceState(outState: Bundle) {
        Icepick.saveInstanceState(this, outState)
        super.onSaveInstanceState(outState)
    }

    inline fun performAction(action: () -> Unit): Boolean {
        action()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.run { MovieGuideApp.getRefWatcher(this).watch(this) }
    }
}