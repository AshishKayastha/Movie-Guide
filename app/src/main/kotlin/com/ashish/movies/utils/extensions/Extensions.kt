package com.ashish.movies.utils.extensions

import android.content.res.Resources

/**
 * Created by Ashish on Dec 31.
 */
fun Float.dpToPx() = this * Resources.getSystem().displayMetrics.density

fun Float.spToPx() = this * Resources.getSystem().displayMetrics.scaledDensity

fun String?.isNotNullOrEmpty() = !this.isNullOrEmpty()