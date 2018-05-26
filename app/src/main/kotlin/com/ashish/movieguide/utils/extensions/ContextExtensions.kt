package com.ashish.movieguide.utils.extensions

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Uri
import android.support.annotation.ArrayRes
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.ashish.movieguide.ui.widget.CustomToast

/**
 * Created by Ashish on Jan 08.
 */
@ColorInt
fun Context.getColorCompat(@ColorRes colorResId: Int) = ContextCompat.getColor(this, colorResId)

fun Context.getDrawableCompat(@DrawableRes drawableResId: Int): Drawable? {
    return ContextCompat.getDrawable(this, drawableResId)
}

fun Context.showToast(messageId: Int, isError: Boolean = true) {
    if (isError) {
        CustomToast.showErrorToast(this, getString(messageId))
    } else {
        CustomToast.showSuccessToast(this, getString(messageId))
    }
}

val Context.inputMethodManager: InputMethodManager?
    get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

val Context.connectivityManager: ConnectivityManager
    get() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

fun Context.getStringArray(@ArrayRes arrayId: Int): Array<String> = resources.getStringArray(arrayId)

fun Context.openUrl(url: String?) {
    if (url.isNotNullOrEmpty()) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}

fun Context.inflateLayout(@LayoutRes layoutId: Int): View {
    return LayoutInflater.from(this).inflate(layoutId, null)
}