package com.ashish.movieguide.ui.widget

import android.content.Context
import android.graphics.Color
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.widget.ImageView
import android.widget.Toast
import com.ashish.movieguide.R
import com.ashish.movieguide.utils.extensions.find
import com.ashish.movieguide.utils.extensions.getDrawableCompat
import com.ashish.movieguide.utils.extensions.inflateLayout
import com.ashish.movieguide.utils.extensions.tint

object CustomToast {

    @ColorInt private val ERROR_COLOR = Color.parseColor("#D50000")
    @ColorInt private val SUCCESS_COLOR = Color.parseColor("#388E3C")
    @ColorInt private val DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF")

    fun showErrorToast(context: Context, message: CharSequence) {
        val toast = createCustomToast(context, message, R.drawable.ic_cross_white_24dp, ERROR_COLOR)
        toast.show()
    }

    fun showSuccessToast(context: Context, message: CharSequence) {
        val toast = createCustomToast(context, message, R.drawable.ic_check_white_24dp, SUCCESS_COLOR)
        toast.show()
    }

    fun createCustomToast(context: Context, message: CharSequence,
                          @DrawableRes drawableId: Int, @ColorInt tintColor: Int): Toast {

        val toast = Toast(context)
        val toastLayout = context.inflateLayout(R.layout.view_toast)
        val toastIcon = toastLayout.find<ImageView>(R.id.toast_icon)
        val toastTextView = toastLayout.find<FontTextView>(R.id.toast_text)

        toastIcon.background = context.getDrawableCompat(drawableId)

        val toastDrawable = context.getDrawableCompat(R.drawable.bg_toast_frame).tint(tintColor)!!
        toastLayout.background = toastDrawable

        toastTextView.text = message
        toastTextView.setTextColor(DEFAULT_TEXT_COLOR)

        toast.view = toastLayout
        toast.duration = Toast.LENGTH_LONG
        return toast
    }
}