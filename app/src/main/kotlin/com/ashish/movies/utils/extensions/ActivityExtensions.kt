package com.ashish.movies.utils.extensions

import android.app.Activity
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.view.View

/**
 * Created by Ashish on Jan 03.
 */
fun Activity?.getStatusBarPair(): Pair<View, String>? {
    var pair: Pair<View, String>? = null
    val statusBar = this?.findViewById(android.R.id.statusBarBackground)
    if (statusBar != null) pair = Pair.create(statusBar, statusBar.transitionName)
    return pair
}

fun Activity?.getNavigationBarPair(): Pair<View, String>? {
    var pair: Pair<View, String>? = null
    val navigationBar = this?.findViewById(android.R.id.navigationBarBackground)
    if (navigationBar != null) pair = Pair.create(navigationBar, navigationBar.transitionName)
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

fun Activity?.hideKeyboard() {
    if (this != null && currentFocus != null) {
        val imm = this.getImm()
        imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }
}

fun Activity.startActivityWithTransition(viewPair: Pair<View, String>?, intent: Intent) {
    val options = getActivityOptionsCompat(viewPair)
    window.exitTransition = null
    ActivityCompat.startActivity(this, intent, options?.toBundle())
}