package com.ashish.movies.utils.extensions

import android.graphics.Bitmap
import android.support.annotation.ColorInt
import android.support.annotation.FloatRange
import android.support.v4.graphics.ColorUtils
import android.support.v7.graphics.Palette

/**
 * Created by Ashish on Jan 04.
 */
fun Bitmap.isDark(pixelX: Int, pixelY: Int): Boolean {
    // first try palette with a small color quant size
    val palette = Palette.from(this).maximumColorCount(3).generate()
    if (palette.swatches.size > 0) {
        return isDark(palette)
    } else {
        // if palette failed, then check the color of the specified pixel
        return getPixel(pixelX, pixelY).isDark()
    }
}

fun Bitmap.isDark(palette: Palette): Boolean {
    val mostPopulous = palette.getSwatchWithMostPixels() ?: return isDark(width / 2, 0)
    return mostPopulous.hsl.isDark()
}

/**
 * Convert to HSL & check that the lightness value
 */
fun Int.isDark(): Boolean {
    val hsl = FloatArray(3)
    ColorUtils.colorToHSL(this, hsl)
    return hsl.isDark()
}

fun FloatArray.isDark() = this[2] < 0.5f

@ColorInt
fun Int.scrimify(isDark: Boolean, @FloatRange(from = 0.0, to = 1.0) lightnessMultiplier: Float = 0.075f): Int {
    val hsl = FloatArray(3)
    var lightnessMultiplier1 = lightnessMultiplier
    ColorUtils.colorToHSL(this, hsl)

    if (!isDark) {
        lightnessMultiplier1 += 1f
    } else {
        lightnessMultiplier1 = 1f - lightnessMultiplier1
    }

    hsl[2] = Math.max(0f, Math.min(1f, hsl[2] * lightnessMultiplier1))
    return ColorUtils.HSLToColor(hsl)
}