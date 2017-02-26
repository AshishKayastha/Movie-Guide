package com.ashish.movieguide.ui.common.palette

import android.graphics.Color
import com.ashish.movieguide.R
import com.ashish.movieguide.ui.base.recyclerview.BaseContentHolder
import com.ashish.movieguide.utils.extensions.animateBackgroundColorChange
import com.ashish.movieguide.utils.extensions.animateTextColorChange
import com.ashish.movieguide.utils.extensions.getColorCompat
import com.ashish.movieguide.utils.extensions.setPaletteColor
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.ImageViewTarget

/**
 * Created by Ashish on Dec 31.
 */
class PaletteImageViewTarget(private val holder: BaseContentHolder<*>)
    : ImageViewTarget<PaletteBitmap>(holder.posterImage) {

    private val primaryTextColor = holder.itemView.context.getColorCompat(R.color.primary_text_light)
    private val secondaryTextColor = holder.itemView.context.getColorCompat(R.color.secondary_text_light)

    override fun onResourceReady(paletteBitmap: PaletteBitmap?, animation: GlideAnimation<in PaletteBitmap>?) {
        if (animation == null || !animation.animate(paletteBitmap, this)) {
            setResource(paletteBitmap)
        }

        paletteBitmap.setPaletteColor { swatch ->
            with(holder) {
                contentView.animateBackgroundColorChange(Color.TRANSPARENT, swatch.rgb)
                contentTitle.animateTextColorChange(primaryTextColor, swatch.titleTextColor)
                contentSubtitle.animateTextColorChange(secondaryTextColor, swatch.bodyTextColor)
            }
        }
    }

    override fun setResource(paletteBitmap: PaletteBitmap?) = super.view.setImageBitmap(paletteBitmap?.bitmap)
}