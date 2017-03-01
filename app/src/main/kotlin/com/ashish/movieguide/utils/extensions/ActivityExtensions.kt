package com.ashish.movieguide.utils.extensions

import android.app.Activity
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.view.View

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
    ActivityCompat.startActivity(this, intent, options?.toBundle())
}