package com.ashish.movieguide.ui.widget

import android.content.Context
import android.graphics.Color
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.widget.ImageView
import android.widget.Toast
import com.ashish.movieguide.R
import com.ashish.movieguide.utils.extensions.getDrawableCompat
import com.ashish.movieguide.utils.extensions.inflateLayout
import com.ashish.movieguide.utils.extensions.tint

object CustomToast {

    const val TOAST_TYPE_INFO = 0
    const val TOAST_TYPE_SUCCESS = 1
    const val TOAST_TYPE_ERROR = 2

    @ColorInt private val INFO_COLOR = Color.parseColor("#3C3C3C")
    @ColorInt private val ERROR_COLOR = Color.parseColor("#D50000")
    @ColorInt private val SUCCESS_COLOR = Color.parseColor("#388E3C")
    @ColorInt private val DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF")

    fun showToast(context: Context, message: CharSequence, msgType: Int = TOAST_TYPE_ERROR) {
        when (msgType) {
            TOAST_TYPE_INFO -> showInfoToast(context, message)
            TOAST_TYPE_SUCCESS -> showSuccessToast(context, message)
            TOAST_TYPE_ERROR -> showErrorToast(context, message)
        }
    }

    private fun showInfoToast(context: Context, message: CharSequence) {
        val toast = createCustomToast(context, message, R.drawable.ic_info_white_24dp, INFO_COLOR)
        toast.show()
    }

    private fun showSuccessToast(context: Context, message: CharSequence) {
        val toast = createCustomToast(context, message, R.drawable.ic_check_white_24dp, SUCCESS_COLOR)
        toast.show()
    }

    private fun showErrorToast(context: Context, message: CharSequence) {
        val toast = createCustomToast(context, message, R.drawable.ic_alert_white_24dp, ERROR_COLOR)
        toast.show()
    }

    private fun createCustomToast(
            context: Context,
            message: CharSequence,
            @DrawableRes drawableId: Int,
            tintColor: Int
    ): Toast {
        val toast = Toast(context)
        val toastLayout = context.inflateLayout(R.layout.view_toast)
        val toastIcon = toastLayout.findViewById<ImageView>(R.id.toast_icon)
        val toastTextView = toastLayout.findViewById<FontTextView>(R.id.toast_text)

        toastIcon.background = context.getDrawableCompat(drawableId)
        if (tintColor != 0) {
            val drawable = context.getDrawableCompat(R.drawable.bg_toast_frame).tint(tintColor)
            toastLayout.background = drawable
        }

        toastTextView.text = message
        toastTextView.setTextColor(DEFAULT_TEXT_COLOR)

        toast.view = toastLayout
        toast.duration = Toast.LENGTH_LONG
        return toast
    }
}