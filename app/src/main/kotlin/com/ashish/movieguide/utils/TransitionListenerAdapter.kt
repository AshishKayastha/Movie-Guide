package com.ashish.movieguide.utils

import android.transition.Transition
import timber.log.Timber

/**
 * Created by Ashish on Jan 16.
 */
abstract class TransitionListenerAdapter : Transition.TransitionListener {
    override fun onTransitionStart(transition: Transition) {
        Timber.v("onTransitionStart")
    }

    override fun onTransitionPause(transition: Transition) {
        Timber.v("onTransitionPause")
    }

    override fun onTransitionResume(transition: Transition) {
        Timber.v("onTransitionResume")
    }

    override fun onTransitionEnd(transition: Transition) {
    }

    override fun onTransitionCancel(transition: Transition) {
    }
}