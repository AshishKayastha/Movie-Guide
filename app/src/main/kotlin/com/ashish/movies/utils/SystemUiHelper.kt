package com.ashish.movies.utils

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.View

class SystemUiHelper(activity: Activity, private var listener: OnVisibilityChangeListener? = null)
    : View.OnSystemUiVisibilityChangeListener {

    private val hideRunnable: Runnable
    private val decorView = activity.window.decorView
    private val handler: Handler = Handler(Looper.getMainLooper())

    var isShowing = true
        set(value) {
            field = value
            listener?.onVisibilityChange(field)
        }

    init {
        hideRunnable = HideRunnable()
        decorView.setOnSystemUiVisibilityChangeListener(this)
    }

    fun show() {
        removeQueuedRunnables()
        decorView.systemUiVisibility = showSystemUi()
        decorView.requestLayout()
    }

    private fun showSystemUi(): Int {
        return (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

    fun hide() {
        removeQueuedRunnables()
        decorView.systemUiVisibility = hideSystemUi()
        decorView.requestLayout()
    }

    private fun hideSystemUi(): Int {
        return (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE)
    }

    fun delayHide(delayMillis: Long) {
        removeQueuedRunnables()
        handler.postDelayed(hideRunnable, delayMillis)
    }

    private fun removeQueuedRunnables() {
        handler.removeCallbacks(hideRunnable)
    }

    override fun onSystemUiVisibilityChange(visibility: Int) {
        isShowing = visibility == 0
    }

    fun removeVisibilityChangeListener() {
        listener = null
        decorView.setOnSystemUiVisibilityChangeListener(null)
    }

    interface OnVisibilityChangeListener {

        fun onVisibilityChange(visible: Boolean)
    }

    private inner class HideRunnable : Runnable {
        override fun run() = hide()
    }
}