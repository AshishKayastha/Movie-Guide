package com.ashish.movies.ui.common.palette

import android.support.v7.graphics.Palette
import com.ashish.movies.ui.base.recyclerview.BaseContentHolder
import com.ashish.movies.utils.extensions.getSwatchWithMostPixels
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.ImageViewTarget

/**
 * Created by Ashish on Dec 31.
 */
class PaletteImageViewTarget(val holder: BaseContentHolder<*>) : ImageViewTarget<PaletteBitmap>(holder.posterImage) {

    override fun onResourceReady(resource: PaletteBitmap?, glideAnimation: GlideAnimation<in PaletteBitmap>?) {
        if (glideAnimation == null || !glideAnimation.animate(resource, this)) {
            setResource(resource)
        }

        setColors(resource?.palette)
    }

    override fun setResource(resource: PaletteBitmap?) = super.view.setImageBitmap(resource?.bitmap)

    private fun setColors(palette: Palette?) {
        val swatch = palette.getSwatchWithMostPixels()
        if (swatch != null) {
            with(swatch) {
                holder.contentView.setBackgroundColor(rgb)
                holder.contentTitle.setTextColor(titleTextColor)
                holder.contentSubtitle.setTextColor(bodyTextColor)
            }
        }
    }
}