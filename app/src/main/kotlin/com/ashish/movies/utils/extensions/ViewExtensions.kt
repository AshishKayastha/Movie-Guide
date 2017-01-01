package com.ashish.movies.utils.extensions

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.support.design.widget.Snackbar
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import com.ashish.movies.utils.FontUtils

/**
 * Created by Ashish on Dec 27.
 */
fun View.setVisibility(visible: Boolean) {
    visibility = if (visible) VISIBLE else GONE
}

fun View.show() {
    visibility = VISIBLE
}

fun View.hide() {
    visibility = GONE
}

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View? {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun View.showSnackBar(message: CharSequence, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, message, duration).show()
}

fun View.showSnackBar(messageId: Int, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, messageId, duration).show()
}

fun TextView.changeTypeface() {
    typeface = FontUtils.getTypeface(context, FontUtils.MONTSERRAT_REGULAR)
}

fun View.animateBackgroundColorChange(startColor: Int, endColor: Int) {
    animateColorChange(startColor, endColor, { setBackgroundColor(it) })
}

fun TextView.animateTextColorChange(startColor: Int, endColor: Int) {
    animateColorChange(startColor, endColor, { setTextColor(it) })
}

fun animateColorChange(startColor: Int, endColor: Int, func: (color: Int) -> Unit) {
    val colorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), startColor, endColor)
    colorAnimator.apply {
        addUpdateListener { animation -> func(animation.animatedValue as Int) }
        duration = 800L
        interpolator = FastOutSlowInInterpolator()
        start()
    }
}