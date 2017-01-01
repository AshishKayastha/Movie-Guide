package com.ashish.movies.ui.common.palette

import android.graphics.Color
import android.support.v7.graphics.Palette
import com.ashish.movies.R
import com.ashish.movies.ui.base.recyclerview.BaseContentHolder
import com.ashish.movies.utils.extensions.animateBackgroundColorChange
import com.ashish.movies.utils.extensions.animateTextColorChange
import com.ashish.movies.utils.extensions.getColorCompat
import com.ashish.movies.utils.extensions.getSwatchWithMostPixels
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.ImageViewTarget

/**
 * Created by Ashish on Dec 31.
 */
class PaletteImageViewTarget(val holder: BaseContentHolder<*>) : ImageViewTarget<PaletteBitmap>(holder.posterImage) {

    private val primaryTextColor = holder.itemView.context.getColorCompat(R.color.primary_text_light)
    private val secondaryTextColor = holder.itemView.context.getColorCompat(R.color.secondary_text_light)

    override fun onResourceReady(resource: PaletteBitmap?, glideAnimation: GlideAnimation<in PaletteBitmap>?) {
        if (glideAnimation == null || !glideAnimation.animate(resource, this)) {
            setResource(resource)
        }

        setColors(resource?.palette)
    }

    override fun setResource(resource: PaletteBitmap?) = super.view.setImageBitmap(resource?.bitmap)

    private fun setColors(palette: Palette?) {
        palette.getSwatchWithMostPixels()?.apply {
            with(holder) {
                contentView.animateBackgroundColorChange(Color.TRANSPARENT, rgb)
                contentTitle.animateTextColorChange(primaryTextColor, titleTextColor)
                contentSubtitle.animateTextColorChange(secondaryTextColor, bodyTextColor)
            }
        }
    }
}