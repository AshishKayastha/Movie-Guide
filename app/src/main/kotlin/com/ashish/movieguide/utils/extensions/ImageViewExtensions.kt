package com.ashish.movieguide.utils.extensions

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.ashish.movieguide.R
import com.ashish.movieguide.utils.glide.GlideApp
import com.ashish.movieguide.utils.glide.palette.PaletteBitmap
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.ImageViewTarget

@SuppressLint("CheckResult")
inline fun ImageView.loadPaletteBitmap(imageUrl: String?, width: Int = 0, height: Int = 0,
                                       crossinline action: ((PaletteBitmap?) -> Unit)) {
    if (imageUrl.isNotNullOrEmpty()) {
        val builder = GlideApp.with(context)
                .`as`(PaletteBitmap::class.java)
                .load(imageUrl)

        if (width > 0 && height > 0) {
            builder.apply(RequestOptions().override(width, height))
        }

        builder.into(object : ImageViewTarget<PaletteBitmap>(this) {
            override fun setResource(paletteBitmap: PaletteBitmap?) {
                setImageBitmap(paletteBitmap?.bitmap)
                action.invoke(paletteBitmap)
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