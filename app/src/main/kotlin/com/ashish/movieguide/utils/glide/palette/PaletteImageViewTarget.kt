package com.ashish.movieguide.utils.glide.palette

import android.graphics.Bitmap
import android.graphics.Color
import android.support.v7.graphics.Palette
import com.ashish.movieguide.R
import com.ashish.movieguide.ui.base.recyclerview.BaseContentHolder
import com.ashish.movieguide.utils.extensions.animateBackgroundColorChange
import com.ashish.movieguide.utils.extensions.animateTextColorChange
import com.ashish.movieguide.utils.extensions.getColorCompat
import com.ashish.movieguide.utils.extensions.setPaletteColor
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.transition.Transition

/**
 * Created by Ashish on Dec 31.
 */
class PaletteImageViewTarget(
        private val holder: BaseContentHolder<*>
) : ImageViewTarget<Bitmap>(holder.posterImage) {

    private val primaryTextColor = holder.itemView.context.getColorCompat(R.color.primary_text_light)
    private val secondaryTextColor = holder.itemView.context.getColorCompat(R.color.secondary_text_light)

    override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
        super.onResourceReady(bitmap, transition)
        val palette = Palette.from(bitmap).generate()
        palette.setPaletteColor { swatch ->
            with(holder) {
                posterImage.setImageBitmap(bitmap)
                contentView.animateBackgroundColorChange(Color.TRANSPARENT, swatch.rgb)
                contentTitle.animateTextColorChange(primaryTextColor, swatch.titleTextColor)
                contentSubtitle.animateTextColorChange(secondaryTextColor, swatch.bodyTextColor)
            }
        }
    }

    override fun setResource(bitmap: Bitmap?) {}
}