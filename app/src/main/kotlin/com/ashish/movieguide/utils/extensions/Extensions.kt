package com.ashish.movieguide.utils.extensions

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.ColorInt
import android.support.v4.graphics.drawable.DrawableCompat
import com.evernote.android.state.StateSaver

/**
 * Created by Ashish on Dec 31.
 */
fun Float.dpToPx(): Float = this * Resources.getSystem().displayMetrics.density

fun Float.spToPx(): Float = this * Resources.getSystem().displayMetrics.scaledDensity

fun isApiOrAbove(version: Int): Boolean = Build.VERSION.SDK_INT >= version

inline fun isMarshmallowOrAbove(crossinline func: () -> Unit) {
    if (isApiOrAbove(23)) func()
}

fun runDelayed(delayMillis: Long, action: () -> Unit) {
    Handler().postDelayed(Runnable(action), delayMillis)
}

fun Drawable?.tint(@ColorInt color: Int): Drawable? {
    if (this != null) {
        val wrapped = DrawableCompat.wrap(this)
        DrawableCompat.setTint(wrapped, color)
        return wrapped
    }
    return this
}

fun <T> Collection<T>?.isNotNullOrEmpty(): Boolean = this != null && size != 0

/**
 * An extension function to either add or remove an item from [MutableCollection].
 * It will remove given item if the item is already in the collection else it will
 * add it to the collection.
 * @param item any item that should be added or removed from [MutableCollection]
 */
fun <T> MutableCollection<T>.addOrRemove(item: T) {
    if (contains(item)) remove(item) else add(item)
}

fun <T> MutableCollection<T>.addAllIfNotNull(item: Collection<T>?) {
    if (item.isNotNullOrEmpty()) addAll(item!!)
}

/**
 * An extension function which will either get extras from the [Bundle] passed
 * through [android.content.Intent] or as an arguments for [android.support.v4.app.Fragment]
 * or restore the saved data from the given [Bundle] using [StateSaver] depending upon
 * whether the savedInstanceState is null or not.
 *
 * @param target type of object to restore data from; most likely [android.app.Activity]
 * or [android.support.v4.app.Fragment]
 * @param getExtras a function to handle getting data passed through [android.content.Intent]
 * or as an arguments for [android.support.v4.app.Fragment]
 */
@SuppressLint("NonMatchingStateSaverCalls")
inline fun <T> Bundle?.getExtrasOrRestore(target: T, crossinline getExtras: () -> Unit) {
    if (this == null) {
        getExtras()
    } else {
        StateSaver.restoreInstanceState(target, this)
    }
}

/**
 * Convert minutes to day, hour, minutes
 */
fun Int?.getDayHourMinutes(): Triple<String, String, String> {
    return if (this != null && this > 0) {
        val oneDay = 24 * 60
        val day = this / oneDay
        val hour = this % oneDay / 60
        val minute = this % oneDay % 60
        Triple(day.toString(), hour.toString(), minute.toString())
    } else {
        Triple("0", "0", "0")
    }
}

inline fun performAction(crossinline action: () -> Unit): Boolean {
    action()
    return true
}