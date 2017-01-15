package com.ashish.movies.utils.systemuihelper

import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity

/**
 * Helper for controlling the visibility of the System UI across the various API levels.
 * The level specifies the extent to which the System UI's visibility is changed when you call [.hide] or [.toggle].
 */
class SystemUiHelper(appCompatActivity: AppCompatActivity, level: Int = LEVEL_IMMERSIVE,
                     flags: Int = FLAG_IMMERSIVE_STICKY,
                     private var listener: SystemUiHelper.OnVisibilityChangeListener? = null) {

    private val hideRunnable: Runnable = HideRunnable()
    private val handler: Handler = Handler(Looper.getMainLooper())
    private val uiHelper: SystemUiHelperImpl = SystemUiHelperImpl(appCompatActivity, level, flags, listener)

    val isShowing = uiHelper.isShowing

    /**
     * Show the system UI. Any currently queued delayed hide requests will be removed.
     */
    fun show() {
        // Ensure that any currently queued hide calls are removed
        removeQueuedRunnables()
        uiHelper.show()
    }

    /**
     * Hide the system UI. Any currently queued delayed hide requests will be removed.
     */
    fun hide() {
        // Ensure that any currently queued hide calls are removed
        removeQueuedRunnables()
        uiHelper.hide()
    }

    /**
     * Request that the system UI is hidden after a delay.
     * Any currently queued delayed hide requests will be removed.

     * @param delayMillis The delay (in milliseconds) until the Runnable will be executed.
     */
    fun delayHide(delayMillis: Long) {
        // Ensure that any currently queued hide calls are removed
        removeQueuedRunnables()
        handler.postDelayed(hideRunnable, delayMillis)
    }

    private fun removeQueuedRunnables() {
        // Ensure that any currently queued hide calls are removed
        handler.removeCallbacks(hideRunnable)
    }

    fun removeVisibilityChangeListener() {
        listener = null
        uiHelper.removeVisibilityChangeListener()
    }

    /**
     * A callback interface used to listen for system UI visibility changes.
     */
    interface OnVisibilityChangeListener {
        /**
         * Called when the system UI visibility has changed.
         *
         * @param visible True if the system UI is visible.
         */
        fun onVisibilityChange(visible: Boolean)
    }

    private inner class HideRunnable : Runnable {
        override fun run() = hide()
    }

    companion object {

        /**
         * In this level, the helper will toggle low profile mode.
         */
        const val LEVEL_LOW_PROFILE = 0

        /**
         * In this level, the helper will toggle the visibility of the status bar.
         * If there is a navigation bar, it will toggle low profile mode.
         */
        const val LEVEL_HIDE_STATUS_BAR = 1

        /**
         * In this level, the helper will toggle the visibility of the navigation bar
         * (if present and if possible) and status bar. In cases where the navigation
         * bar is present but cannot be hidden, it will toggle low profile mode.
         */
        const val LEVEL_LEAN_BACK = 2

        /**
         * In this level, the helper will toggle the visibility of the navigation bar
         * (if present and if possible) and status bar, in an immersive mode. This means that the app
         * will continue to receive all touch events. The user can reveal the system bars with an
         * inward swipe along the region where the system bars normally appear.
         *
         * The [.FLAG_IMMERSIVE_STICKY] flag can be used to control how the system bars are
         * displayed.
         */
        const val LEVEL_IMMERSIVE = 3

        /**
         * Used with [.LEVEL_IMMERSIVE]. When this flag is set, an inward swipe in the system
         * bars areas will cause the system bars to temporarily appear in a semi-transparent state,
         * but no flags are cleared, and your system UI visibility change listeners are not triggered.
         * The bars automatically hide again after a short delay, or if the user interacts with the
         * middle of the screen.
         */
        const val FLAG_IMMERSIVE_STICKY = 0x2
    }
}