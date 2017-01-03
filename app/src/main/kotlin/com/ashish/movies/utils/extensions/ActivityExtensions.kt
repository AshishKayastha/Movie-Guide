package com.ashish.movies.utils.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.view.View

/**
 * Created by Ashish on Jan 03.
 */
@SuppressLint("InlinedApi")
fun Activity?.getStatusBarPair(): Pair<View, String>? {
    var pair: Pair<View, String>? = null
    isLollipopOrAbove {
        val statusBar = this?.findViewById(android.R.id.statusBarBackground)
        if (statusBar != null) pair = Pair.create(statusBar, statusBar.transitionName)
    }
    return pair
}

@SuppressLint("InlinedApi")
fun Activity?.getNavigationBarPair(): Pair<View, String>? {
    var pair: Pair<View, String>? = null
    isLollipopOrAbove {
        @SuppressLint("InlinedApi")
        val navigationBar = this?.findViewById(android.R.id.navigationBarBackground)
        if (navigationBar != null) pair = Pair.create(navigationBar, navigationBar.transitionName)
    }
    return pair
}

fun Activity?.getActivityOptionsCompat(posterImagePair: Pair<View, String>?): ActivityOptionsCompat? {
    var options: ActivityOptionsCompat? = null

    val statusBarPair = getStatusBarPair()
    val navigationBarPair = getNavigationBarPair()

    if (posterImagePair != null && statusBarPair != null && navigationBarPair != null) {
        options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, posterImagePair, statusBarPair,
                navigationBarPair)

    } else if (posterImagePair != null && statusBarPair != null) {
        options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, posterImagePair, statusBarPair)

    } else if (posterImagePair != null && navigationBarPair != null) {
        options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, posterImagePair, navigationBarPair)
    }

    return options
}