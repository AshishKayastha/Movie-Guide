package com.ashish.movieguide.utils.extensions

import android.app.Activity
import android.content.Intent
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.view.View
import com.ashish.movieguide.app.MovieGuideApp
import com.ashish.movieguide.utils.glide.palette.PaletteBitmap

/**
 * Created by Ashish on Jan 03.
 */
fun Activity?.hideKeyboard() {
    if (this != null && currentFocus != null) {
        inputMethodManager?.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }
}

fun Activity.startActivityWithTransition(viewPair: Pair<View, String>?, intent: Intent) {
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, viewPair)
    window.exitTransition = null
    ActivityCompat.startActivity(this, intent, options.toBundle())
}

inline fun Activity.setTopBarColorAndAnimate(
        paletteBitmap: PaletteBitmap?,
        collapsingToolbar: CollapsingToolbarLayout,
        tintIcons: () -> Unit
) {
    if (paletteBitmap != null) {
        val palette = paletteBitmap.palette
        val isDark = paletteBitmap.bitmap.isDark(palette)

        if (!isDark) {
            window.decorView.setLightStatusBar()
            tintIcons()
        }

        val rgbColor = palette.getSwatchWithMostPixels()?.rgb

        /*
          Animate status bar color change between previous status bar color
          and the color extracted from bitmap through palette.
         */
        if (rgbColor != null) {
            val newStatusBarColor = rgbColor.scrimify(isDark)
            collapsingToolbar.setContentScrimColor(rgbColor)
            animateColorChange(window.statusBarColor, newStatusBarColor, 500L) { color ->
                window.statusBarColor = color
            }
        }
    }
}

fun Activity?.watchFragmentLeaks() {
    this?.run { MovieGuideApp.getRefWatcher(this).watch(this) }
}