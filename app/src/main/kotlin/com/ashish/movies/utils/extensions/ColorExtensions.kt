package com.ashish.movies.utils.extensions

import android.graphics.Bitmap
import android.support.annotation.ColorInt
import android.support.annotation.FloatRange
import android.support.v4.graphics.ColorUtils
import android.support.v7.graphics.Palette

/**
 * Determines if a given bitmap is dark. This extracts a palette inline so should not be called
 * with a large image!! If palette fails then check the color of the specified pixel
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

/**
 * Check that the lightness value (0–1)
 */
fun FloatArray.isDark() = this[2] < 0.5f

/**
 * Calculate a variant of the color to make it more suitable for overlaying information. Light
 * colors will be lightened and dark colors will be darkened
 *
 * @param isDark              whether color is light or dark
 * @param lightnessMultiplier the amount to modify the color e.g. 0.1f will alter it by 10%
 * @return the adjusted color
 */
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