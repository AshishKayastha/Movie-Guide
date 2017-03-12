package com.ashish.movieguide.utils.extensions

import android.widget.ImageView
import com.ashish.movieguide.R
import com.ashish.movieguide.ui.common.palette.PaletteBitmap
import com.ashish.movieguide.utils.CircularTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.ImageViewTarget

inline fun ImageView.loadPaletteBitmap(imageUrl: String?, width: Int = 0, height: Int = 0,
                                       crossinline action: ((PaletteBitmap?) -> Unit)) {
    if (imageUrl.isNotNullOrEmpty()) {
        val builder = Glide.with(context)
                .transcodePaletteBitmap(context)
                .load(imageUrl)

        if (width > 0 && height > 0) {
            builder.override(width, height)
        }

        builder.into(object : ImageViewTarget<PaletteBitmap>(this) {
            override fun setResource(paletteBitmap: PaletteBitmap?) {
                setImageBitmap(paletteBitmap?.bitmap)
                action.invoke(paletteBitmap)
            }
        })
    } else {
        Glide.clear(this)
    }
}

fun ImageView.loadCircularImage(imageUrl: String?) {
    if (imageUrl.isNotNullOrEmpty()) {
        Glide.with(context)
                .load(imageUrl)
                .bitmapTransform(CircularTransformation(Glide.get(context).bitmapPool))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_person_white_80dp)
                .error(R.drawable.ic_person_white_80dp)
                .into(this)
    } else {
        Glide.clear(this)
    }
}