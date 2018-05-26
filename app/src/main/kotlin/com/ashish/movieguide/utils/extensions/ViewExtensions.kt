package com.ashish.movieguide.utils.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.support.annotation.StringRes
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.Snackbar
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import com.ashish.movieguide.R
import com.ashish.movieguide.utils.FontUtils

/**
 * Created by Ashish on Dec 27.
 */
inline fun <reified T : View> View.find(id: Int): T = findViewById(id)

fun View.setVisibility(visible: Boolean) {
    if (visible) show() else hide()
}

fun View.show() {
    if (visibility != VISIBLE) visibility = VISIBLE
}

fun View.hide(viewGone: Boolean = true) {
    visibility = if (viewGone) GONE else INVISIBLE
}

fun View.showSnackBar(
        messageId: Int,
        duration: Int = Snackbar.LENGTH_LONG,
        @StringRes actionBtnTextId: Int = android.R.string.ok,
        action: (() -> Unit)? = null
) {

    val snackbar = Snackbar.make(this, messageId, duration)
            .setAction(actionBtnTextId, { action?.invoke() })

    snackbar.changeSnackBarFont(android.support.design.R.id.snackbar_text)
    snackbar.changeSnackBarFont(android.support.design.R.id.snackbar_action)
    snackbar.view.setBackgroundColor(context.getColorCompat(R.color.primary_gradient_end_color))

    snackbar.show()
}

fun Snackbar.changeSnackBarFont(viewId: Int) = view.find<TextView>(viewId).changeTypeface()

fun View.animateBackgroundColorChange(startColor: Int, endColor: Int) {
    animateColorChange(startColor, endColor, onAnimationUpdate = { setBackgroundColor(it) })
}

inline fun animateColorChange(
        startColor: Int,
        endColor: Int,
        duration: Long = 800L,
        crossinline onAnimationUpdate: (color: Int) -> Unit
) {
    if (startColor != endColor) {
        val colorAnimator = ValueAnimator.ofArgb(startColor, endColor)
        colorAnimator.apply {
            this.duration = duration
            interpolator = FastOutSlowInInterpolator()
            addUpdateListener { animation -> onAnimationUpdate(animation.animatedValue as Int) }
            start()
        }
    }
}

fun View.getPosterImagePair(@StringRes transitionNameId: Int): Pair<View, String>? {
    val posterImageView = find<ImageView>(R.id.poster_image)
    return Pair.create(posterImageView, context.getString(transitionNameId))
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

fun View?.showKeyboard() {
    if (this == null) return

    requestFocus()

    val imm = context.inputMethodManager!!
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    imm.showSoftInput(this, 0)

    if (!imm.isActive(this)) {
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}

inline fun View.startCircularRevealAnimation(
        cx: Int,
        cy: Int,
        startRadius: Float,
        endRadius: Float,
        duration: Long = 400L,
        crossinline animationEnd: () -> Unit
) {
    val animator = ViewAnimationUtils.createCircularReveal(this, cx, cy, startRadius, endRadius)
    animator.duration = duration
    animator.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {}

        override fun onAnimationEnd(animation: Animator?) = animationEnd()

        override fun onAnimationCancel(animation: Animator?) = animationEnd()

        override fun onAnimationRepeat(animation: Animator?) {}
    })

    show()
    animator.start()
}

inline fun View.onLayoutLaid(crossinline action: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            action.invoke()
        }
    })
}

fun View.showOrHideWithAnimation(show: Boolean, viewGone: Boolean = true) {
    if (show) showWithAnimation() else hideWithAnimation(viewGone)
}

fun View.showWithAnimation() {
    if (visibility == GONE || visibility == INVISIBLE) {
        alpha = 0F
        animate().alpha(1F)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) = show()
                })
                .start()
    }
}

fun View.hideWithAnimation(viewGone: Boolean = true) {
    if (visibility == VISIBLE) {
        alpha = 1F
        animate().alpha(0F)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) = hide(viewGone)
                })
                .start()
    }
}

fun CollapsingToolbarLayout.changeTitleTypeface() {
    val regularFont = FontUtils.getTypeface(context, FontUtils.MONTSERRAT_REGULAR)
    setExpandedTitleTypeface(regularFont)
    setCollapsedTitleTypeface(regularFont)
}