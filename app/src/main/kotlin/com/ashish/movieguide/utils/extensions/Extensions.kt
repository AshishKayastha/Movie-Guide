package com.ashish.movieguide.utils.extensions

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.ColorInt
import android.support.v4.graphics.drawable.DrawableCompat
import com.ashish.movieguide.data.models.tmdb.AccountState
import com.ashish.movieguide.ui.common.palette.PaletteBitmap
import com.ashish.movieguide.ui.common.palette.PaletteBitmapTranscoder
import com.bumptech.glide.BitmapRequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import icepick.Icepick

/**
 * Created by Ashish on Dec 31.
 */
fun Float.dpToPx() = this * Resources.getSystem().displayMetrics.density

fun Float.spToPx() = this * Resources.getSystem().displayMetrics.scaledDensity

fun RequestManager.transcodePaletteBitmap(context: Context): BitmapRequestBuilder<String, PaletteBitmap> {
    return fromString()
            .asBitmap()
            .transcode(PaletteBitmapTranscoder(context), PaletteBitmap::class.java)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
}

fun isApiOrAbove(version: Int) = Build.VERSION.SDK_INT >= version

inline fun isMarshmallowOrAbove(func: () -> Unit) {
    if (isApiOrAbove(23)) func()
}

fun <T> Collection<T>?.isNotNullOrEmpty() = this != null && size != 0

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

/**
 * An extension function to either add or remove an item from [MutableCollection].
 * It will remove given item if the item is already in the collection else it will
 * add it to the collection.
 * @param item any item that should be added or removed from [MutableCollection]
 */
fun <T> MutableCollection<T>.addOrRemove(item: T) {
    if (contains(item)) remove(item) else add(item)
}

/**
 * An extension function which will either get extras from the [Bundle] passed
 * through [android.content.Intent] or as an arguments for [android.support.v4.app.Fragment]
 * or restore the saved data from the given [Bundle] using [Icepick] depending upon
 * whether the savedInstanceState is null or not.
 *
 * @param target type of object to restore data from; most likely [android.app.Activity]
 * or [android.support.v4.app.Fragment]
 * @param getExtras a function to handle getting data passed through [android.content.Intent]
 * or as an arguments for [android.support.v4.app.Fragment]
 */
inline fun <T> Bundle?.getExtrasOrRestore(target: T, getExtras: () -> Unit) {
    if (this == null) {
        getExtras()
    } else {
        Icepick.restoreInstanceState(target, this)
    }
}

fun AccountState.getRatingValue(): Double? {
    if (rated is Map<*, *>) {
        @Suppress("UNCHECKED_CAST")
        val ratingMap = rated as Map<String, Double>
        return ratingMap["value"]
    }
    return 0.0
}

/**
 * Convert minutes to day, hour, minutes
 */
fun Int?.getDayHourMinutes(): Triple<String, String, String> {
    if (this != null && this > 0) {
        val oneDay = 24 * 60
        val day = this / oneDay
        val hour = this % oneDay / 60
        val minute = this % oneDay % 60
        return Triple(day.toString(), hour.toString(), minute.toString())
    }

    return Triple("0", "0", "0")
}