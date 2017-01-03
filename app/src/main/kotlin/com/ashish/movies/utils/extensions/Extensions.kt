package com.ashish.movies.utils.extensions

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.support.v4.content.ContextCompat
import com.ashish.movies.ui.common.palette.PaletteBitmap
import com.ashish.movies.ui.common.palette.PaletteBitmapTranscoder
import com.bumptech.glide.BitmapRequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * Created by Ashish on Dec 31.
 */
fun Float.dpToPx() = this * Resources.getSystem().displayMetrics.density

fun Float.spToPx() = this * Resources.getSystem().displayMetrics.scaledDensity

fun String?.isNotNullOrEmpty() = !this.isNullOrEmpty()

fun RequestManager.transcodePaletteBitmap(context: Context): BitmapRequestBuilder<String, PaletteBitmap> {
    return fromString()
            .asBitmap()
            .transcode(PaletteBitmapTranscoder(context), PaletteBitmap::class.java)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
}

fun Context.getColorCompat(colorResId: Int) = ContextCompat.getColor(this, colorResId)

fun isLollipopOrAbove(func: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        func()
    }
}

fun String?.getYearOnly(): String {
    return if (isNotNullOrEmpty()) this!!.slice(0..3) else ""
}