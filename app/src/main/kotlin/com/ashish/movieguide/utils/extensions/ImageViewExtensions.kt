package com.ashish.movieguide.utils.extensions

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v7.graphics.Palette
import android.widget.ImageView
import com.ashish.movieguide.R
import com.ashish.movieguide.utils.glide.GlideApp
import com.ashish.movieguide.utils.glide.PaletteBitmap
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.ImageViewTarget

@SuppressLint("CheckResult")
inline fun ImageView.loadPaletteBitmap(
        imageUrl: String?,
        width: Int = 0,
        height: Int = 0,
        crossinline action: ((PaletteBitmap?) -> Unit)
) {
    if (imageUrl.isNotNullOrEmpty()) {
        val builder = GlideApp.with(context)
                .asBitmap()
                .load(imageUrl)

        if (width > 0 && height > 0) {
            builder.apply(RequestOptions().override(width, height))
        }

        builder.into(object : ImageViewTarget<Bitmap>(this) {
            override fun setResource(bitmap: Bitmap?) {
                if (bitmap != null) {
                    setImageBitmap(bitmap)
                    val palette = Palette.from(bitmap).generate()
                    action.invoke(PaletteBitmap(bitmap, palette))
                }
            }
        })
    } else {
        GlideApp.with(context).clear(this)
    }
}

fun ImageView.loadCircularImage(imageUrl: String?, listener: RequestListener<Drawable>? = null) {
    if (imageUrl.isNotNullOrEmpty()) {
        val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_person_white_80dp)
                .error(R.drawable.ic_person_white_80dp)

        GlideApp.with(context)
                .load(imageUrl)
                .apply(requestOptions)
                .listener(listener)
                .into(this)
    } else {
        GlideApp.with(context).clear(this)
    }
}

fun ImageView.loadImage(imageUrl: String?) {
    GlideApp.with(context).load(imageUrl).into(this)
}