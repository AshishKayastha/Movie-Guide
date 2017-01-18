package com.ashish.movies.utils.extensions

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v7.widget.ActionMenuView
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TypefaceSpan
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
    val snackbar = Snackbar.make(this, messageId, duration)
            .setAction(android.R.string.ok, { })

    snackbar.changeSnackBarFont(android.support.design.R.id.snackbar_text)
    snackbar.changeSnackBarFont(android.support.design.R.id.snackbar_action)

    snackbar.show()
}

fun Snackbar.changeSnackBarFont(viewId: Int) {
    (view.findViewById(viewId) as TextView).changeTypeface()
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
    val colorAnimator = ValueAnimator.ofArgb(startColor, endColor)
    colorAnimator.apply {
        this.duration = duration
        interpolator = FastOutSlowInInterpolator()
        addUpdateListener { animation -> onAnimationUpdate(animation.animatedValue as Int) }
        start()
    }
}

fun View.getPosterImagePair(@StringRes transitionNameId: Int): Pair<View, String>? {
    val posterImageView = findViewById(R.id.poster_image)
    return if (posterImageView != null) Pair.create(posterImageView, context.getString(transitionNameId)) else null
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
                    setImageBitmap(paletteBitmap?.bitmap)
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

fun Menu.changeMenuFont(typefaceSpan: TypefaceSpan) {
    val size = size()
    (0..size - 1)
            .map { getItem(it) }
            .filterNotNull()
            .forEach { menuItem ->
                val spannableString = SpannableString(menuItem.title)
                spannableString.setSpan(typefaceSpan, 0, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                menuItem.title = spannableString
            }
}

fun ViewGroup.changeViewGroupTextFont() {
    val size = childCount
    (0..size - 1)
            .map { getChildAt(it) }
            .filterIsInstance<TextView>()
            .forEach(TextView::changeTypeface)
}

fun View?.showKeyboard() {
    if (this == null) return

    requestFocus()

    val imm = context.getImm()
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    imm.showSoftInput(this, 0)

    if (!imm.isActive(this)) {
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}

fun ViewGroup.getOverflowMenuButton(): ImageView? {
    val count = childCount
    var overflowMenu: ImageView? = null
    for (i in 0..count - 1) {
        val view = getChildAt(i)
        if (view is ImageView && (view.javaClass.simpleName == "OverflowMenuButton"
                || view is ActionMenuView.ActionMenuChildView)) {
            overflowMenu = view
        } else if (view is ViewGroup) {
            overflowMenu = view.getOverflowMenuButton()
        }

        if (overflowMenu != null) break
    }

    return overflowMenu
}