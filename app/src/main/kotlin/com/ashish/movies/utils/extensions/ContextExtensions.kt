package com.ashish.movies.utils.extensions

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

/**
 * Created by Ashish on Jan 08.
 */
fun Context.getColorCompat(colorResId: Int) = ContextCompat.getColor(this, colorResId)

fun Context.showToast(messageId: Int, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, messageId, duration).show()
}

fun Context.getImm(): InputMethodManager {
    return getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
}