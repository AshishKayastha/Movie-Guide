package com.ashish.movies.utils.extensions

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ashish.movies.R
import com.ashish.movies.ui.common.palette.PaletteBitmap
import com.ashish.movies.utils.FontUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.ImageViewTarget


/**
 * Created by Ashish on Dec 27.
 */
fun View.setVisibility(visible: Boolean) {
    if (visible) show() else hide()
}

fun View.show() {
    if (visibility != VISIBLE) visibility = VISIBLE
}

fun View.hide() {
    if (visibility != GONE) visibility = GONE
}

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View? {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun View.showSnackBar(messageId: Int, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, messageId, duration)
            .setAction(android.R.string.ok, { })
            .show()
}

fun TextView.changeTypeface() {
    typeface = FontUtils.getTypeface(context, FontUtils.MONTSERRAT_REGULAR)
}

fun View.animateBackgroundColorChange(startColor: Int, endColor: Int) {
    animateColorChange(startColor, endColor, onAnimationUpdate = { setBackgroundColor(it) })
}

fun TextView.animateTextColorChange(startColor: Int, endColor: Int) {
    animateColorChange(startColor, endColor, onAnimationUpdate = { setTextColor(it) })
}

inline fun animateColorChange(startColor: Int, endColor: Int, duration: Long = 800L,
                              crossinline onAnimationUpdate: (color: Int) -> Unit) {
    val colorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), startColor, endColor)
    colorAnimator.apply {
        addUpdateListener { animation -> onAnimationUpdate(animation.animatedValue as Int) }
        this.duration = duration
        interpolator = FastOutSlowInInterpolator()
        start()
    }
}

fun View.getPosterImagePair(): Pair<View, String>? {
    val posterImageView = findViewById(R.id.poster_image)
    return if (posterImageView != null) Pair.create(posterImageView, posterImageView.transitionName) else null
}

fun TextView.setTitleAndYear(title: String?, releaseDate: String?) {
    val yearOnly = releaseDate.getYearOnly()
    text = if (yearOnly.isNotEmpty()) "$title ($yearOnly)" else "$title"
}

fun ImageView.loadPaletteBitmap(imageUrl: String, func: ((PaletteBitmap?) -> Unit)? = null) {
    Glide.with(context)
            .transcodePaletteBitmap(context)
            .load(imageUrl)
            .into(object : ImageViewTarget<PaletteBitmap>(this) {
                override fun setResource(paletteBitmap: PaletteBitmap?) {
                    super.view.setImageBitmap(paletteBitmap?.bitmap)
                    func?.invoke(paletteBitmap)
                }
            })
}

fun TextView.applyText(text: String?) {
    if (text.isNotNullOrEmpty()) {
        show()
        this.text = text
    } else {
        hide()
    }
}

@SuppressLint("InlinedApi")
fun View.setLightStatusBar() {
    isMarshmallowOrAbove {
        var flags = systemUiVisibility
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        systemUiVisibility = flags
    }
}

fun View.setTransitionName(@StringRes transitionNameId: Int) {
    ViewCompat.setTransitionName(this, context.getString(transitionNameId))
}