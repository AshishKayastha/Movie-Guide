package com.ashish.movies.utils.extensions

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.preference.PreferenceManager
import android.support.annotation.ArrayRes
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

/**
 * Created by Ashish on Jan 08.
 */
@ColorInt
fun Context.getColorCompat(@ColorRes colorResId: Int) = ContextCompat.getColor(this, colorResId)

fun Context.showToast(messageId: Int, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, messageId, duration).show()
}

fun Context.showToast(message: CharSequence, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, duration).show()
}

val Context.inputMethodManager: InputMethodManager?
    get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

val Context.connectivityManager: ConnectivityManager
    get() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

val Context.defaultSharedPreferences: SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(this)

fun Context.getStringArray(@ArrayRes arrayId: Int): Array<String> = resources.getStringArray(arrayId)
