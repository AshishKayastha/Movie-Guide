package com.ashish.movies.utils.extensions

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.ashish.movies.ui.common.palette.PaletteBitmap
import com.ashish.movies.ui.common.palette.PaletteBitmapTranscoder
import com.bumptech.glide.BitmapRequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

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

fun Context.getColorCompat(colorResId: Int) = ContextCompat.getColor(this, colorResId)

@Suppress("unused")
fun Context.showToast(messageId: Int, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, messageId, duration).show()
}

fun isApiOrAbove(version: Int) = Build.VERSION.SDK_INT >= version

inline fun isLollipopOrAbove(func: () -> Unit) {
    if (isApiOrAbove(21)) func()
}

inline fun isMarshmallowOrAbove(func: () -> Unit) {
    if (isApiOrAbove(23)) func()
}

fun <T> Observable<T>.observeOnMainThread(): Observable<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
}