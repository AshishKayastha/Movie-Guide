package com.ashish.movies.extensions

import android.support.v7.graphics.Palette

/**
 * Created by Ashish on Dec 29.
 */
fun Palette?.getSwatchWithMostPopulation(): Palette.Swatch? {
    var mostPopulationSwatch: Palette.Swatch? = null
    if (this != null) {
        for (swatch in this.swatches) {
            if (mostPopulationSwatch == null || swatch.population > mostPopulationSwatch.population) {
                mostPopulationSwatch = swatch
            }
        }
    }
    return mostPopulationSwatch
}