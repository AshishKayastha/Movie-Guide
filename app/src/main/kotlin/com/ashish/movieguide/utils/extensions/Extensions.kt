package com.ashish.movieguide.utils.extensions

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.support.annotation.ColorInt
import android.support.v4.graphics.drawable.DrawableCompat
import com.ashish.movieguide.ui.common.palette.PaletteBitmap
import com.ashish.movieguide.ui.common.palette.PaletteBitmapTranscoder
import com.bumptech.glide.BitmapRequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy

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

fun <T> MutableCollection<T>.addOrRemove(item: T) {
    if (contains(item)) remove(item) else add(item)
}