package com.ashish.movieguide.utils.extensions

import android.widget.ImageView
import com.ashish.movieguide.R
import com.ashish.movieguide.ui.common.palette.PaletteBitmap
import com.ashish.movieguide.utils.CircularTransformation
import com.ashish.movieguide.utils.Logger
import com.bumptech.glide.Glide
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

fun ImageView.loadGravatarImage(gravatarHash: String?) {
    if (gravatarHash.isNotNullOrEmpty()) {
        Logger.v(gravatarHash!!)
        Glide.with(context)
                .load("https://www.gravatar.com/avatar/$gravatarHash.jpg?s=90")
                .bitmapTransform(CircularTransformation(Glide.get(context).bitmapPool))
                .placeholder(R.drawable.ic_person_white_80dp)
                .error(R.drawable.ic_person_white_80dp)
                .into(this)
    } else {
        Glide.clear(this)
    }
}