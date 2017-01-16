package com.ashish.movies.utils

import android.transition.Transition

/**
 * Created by Ashish on Jan 16.
 */
abstract class TransitionListenerAdapter : Transition.TransitionListener {
    override fun onTransitionStart(transition: Transition) {}

    override fun onTransitionPause(transition: Transition) {}

    override fun onTransitionResume(transition: Transition) {}

    override fun onTransitionEnd(transition: Transition) {}

    override fun onTransitionCancel(transition: Transition) {}
}