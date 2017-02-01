package com.ashish.movies.utils.extensions

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.Handler
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

fun isApiOrAbove(version: Int) = Build.VERSION.SDK_INT >= version

inline fun isMarshmallowOrAbove(func: () -> Unit) {
    if (isApiOrAbove(23)) func()
}

fun <T> Observable<T>.observeOnMainThread(): Observable<T> {
    return observeOn(AndroidSchedulers.mainThread())
}

fun <T> Collection<T>?.isNotNullOrEmpty() = this != null && size != 0

fun runDelayed(delayMillis: Long, action: () -> Unit) = Handler().postDelayed(Runnable(action), delayMillis)