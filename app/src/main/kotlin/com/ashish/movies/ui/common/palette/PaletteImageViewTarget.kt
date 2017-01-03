package com.ashish.movies.ui.common.palette

import android.graphics.Color
import com.ashish.movies.R
import com.ashish.movies.ui.base.recyclerview.BaseContentHolder
import com.ashish.movies.utils.extensions.animateBackgroundColorChange
import com.ashish.movies.utils.extensions.animateTextColorChange
import com.ashish.movies.utils.extensions.getColorCompat
import com.ashish.movies.utils.extensions.setPaletteColor
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.ImageViewTarget

/**
 * Created by Ashish on Dec 31.
 */
class PaletteImageViewTarget(val holder: BaseContentHolder<*>) : ImageViewTarget<PaletteBitmap>(holder.posterImage) {

    private val primaryTextColor = holder.itemView.context.getColorCompat(R.color.primary_text_light)

    override fun onResourceReady(resource: PaletteBitmap?, glideAnimation: GlideAnimation<in PaletteBitmap>?) {
        if (glideAnimation == null || !glideAnimation.animate(resource, this)) {
            setResource(resource)
        }

        resource.setPaletteColor { swatch ->
            with(holder) {
                contentTitle.animateBackgroundColorChange(Color.TRANSPARENT, swatch.rgb)
                contentTitle.animateTextColorChange(primaryTextColor, swatch.titleTextColor)
            }
        }
    }

    override fun setResource(resource: PaletteBitmap?) = super.view.setImageBitmap(resource?.bitmap)
}