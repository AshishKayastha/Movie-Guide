package com.ashish.movies.extensions

import android.support.v7.graphics.Palette

/**
 * Created by Ashish on Dec 29.
 */
fun Palette?.getSwatchWithMostPixels(): Palette.Swatch? {
    var mostPixelSwatch: Palette.Swatch? = null
    if (this != null) {
        for (swatch in this.swatches) {
            if (mostPixelSwatch == null || swatch.population > mostPixelSwatch.population) {
                mostPixelSwatch = swatch
            }
        }
    }
    return mostPixelSwatch
}