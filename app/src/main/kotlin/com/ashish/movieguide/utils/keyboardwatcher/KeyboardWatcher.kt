package com.ashish.movieguide.utils.keyboardwatcher

import android.app.Activity
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import android.widget.EditText
import com.ashish.movieguide.di.scopes.ActivityScope
import com.ashish.movieguide.utils.extensions.hideKeyboard
import javax.inject.Inject

/**
 * Created by Ashish on Jan 08.
 */
@ActivityScope
class KeyboardWatcher @Inject constructor(
        private val activity: Activity
) : ViewTreeObserver.OnGlobalLayoutListener {

    companion object {
        private const val MIN_KEYBOARD_HEIGHT_PX = 150
    }

    private val coordinates = IntArray(2)

    private val touchRect: Rect = Rect()
    private val visibleFrameRect: Rect = Rect()

    private lateinit var decorView: View
    private var focusedView: View? = null
    private var lastVisibleDecorViewHeight: Int = 0
    private var touchWasInsideFocusedView: Boolean = false
    private var listener: KeyboardVisibilityListener? = null

    fun watchKeyboard(listener: KeyboardVisibilityListener) {
        this.listener = listener

        val content = activity.findViewById(Window.ID_ANDROID_CONTENT)
        content.isFocusable = true
        content.isFocusableInTouchMode = true

        decorView = activity.window.decorView
        decorView.viewTreeObserver.addOnGlobalLayoutListener(this)

        activity.application.registerActivityLifecycleCallbacks(object : AutoActivityLifecycleCallbacks(activity) {
            override fun onTargetActivityDestroyed() {
                removeListener()
            }
        })
    }

    internal fun removeListener() {
        listener = null
        decorView.viewTreeObserver.removeOnGlobalLayoutListener(this)
    }

    override fun onGlobalLayout() {
        if (listener != null) {
            decorView.getWindowVisibleDisplayFrame(visibleFrameRect)
            val visibleDecorViewHeight = visibleFrameRect.height()

            // Decide whether keyboard is visible from changing decor view height.
            if (lastVisibleDecorViewHeight != 0) {
                if (lastVisibleDecorViewHeight > visibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX) {
                    val currentKeyboardHeight = decorView.height - visibleFrameRect.bottom
                    listener!!.onKeyboardShown(currentKeyboardHeight)

                } else if (lastVisibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX < visibleDecorViewHeight) {
                    listener!!.onKeyboardHidden()
                }
            }

            // Save current decor view height for the next call
            lastVisibleDecorViewHeight = visibleDecorViewHeight
        }
    }

    fun dispatchEditTextTouchEvent(event: MotionEvent, dispatchTouchEvent: Boolean): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                focusedView = activity.currentFocus
                if (focusedView != null) {
                    focusedView!!.getLocationOnScreen(coordinates)

                    touchRect.set(coordinates[0], coordinates[1],
                            coordinates[0] + focusedView!!.width,
                            coordinates[1] + focusedView!!.height)

                    touchWasInsideFocusedView = touchRect.contains(event.x.toInt(), event.y.toInt())
                }
            }

            MotionEvent.ACTION_UP -> if (focusedView != null) {
                // Dispatch to allow new view to (potentially) take focus
                val currentFocus = activity.currentFocus

                /*
                  If the focus is still on the original view and the touch was inside that view,
                  leave the keyboard open.  Otherwise, if the focus is now on another view
                  and that view is an EditText, also leave the keyboard open.
                */
                if (focusedView == currentFocus) {
                    if (touchWasInsideFocusedView) return dispatchTouchEvent
                } else if (currentFocus is EditText) {
                    return dispatchTouchEvent
                }

                /*
                  If the touch was outside the originally focused view and not inside
                  another EditText, so close the keyboard
                */
                activity.hideKeyboard()
                focusedView!!.clearFocus()
                return dispatchTouchEvent
            }
        }
        return dispatchTouchEvent
    }

    interface KeyboardVisibilityListener {

        fun onKeyboardShown(keyboardHeight: Int)

        fun onKeyboardHidden()
    }
}