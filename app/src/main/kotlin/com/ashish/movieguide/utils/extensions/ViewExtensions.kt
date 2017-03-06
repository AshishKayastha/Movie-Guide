package com.ashish.movieguide.utils.extensions

import android.animation.Animator
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
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import com.ashish.movieguide.R
import com.ashish.movieguide.ui.common.palette.PaletteBitmap
import com.ashish.movieguide.utils.FontUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.ImageViewTarget

/**
 * Created by Ashish on Dec 27.
 */
inline fun <reified T : View> View.find(id: Int): T = findViewById(id) as T

val ViewGroup.views: List<View>
    get() = (0 until childCount).map { getChildAt(it) }

fun View.setVisibility(visible: Boolean) {
    if (visible) show() else hide()
}

fun View.show() {
    if (visibility != VISIBLE) visibility = VISIBLE
}

fun View.hide(viewGone: Boolean = true) {
    visibility = if (viewGone) GONE else INVISIBLE
}

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View? {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun View.showSnackBar(messageId: Int, duration: Int = Snackbar.LENGTH_LONG,
                      @StringRes actionBtnTextId: Int = android.R.string.ok, action: (() -> Unit)? = null) {

    val snackbar = Snackbar.make(this, messageId, duration)
            .setAction(actionBtnTextId, { action?.invoke() })

    snackbar.changeSnackBarFont(android.support.design.R.id.snackbar_text)
    snackbar.changeSnackBarFont(android.support.design.R.id.snackbar_action)
    snackbar.view.setBackgroundColor(context.getColorCompat(R.color.primary_gradient_end_color))

    snackbar.show()
}

fun Snackbar.changeSnackBarFont(viewId: Int) = view.find<TextView>(viewId).changeTypeface()

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
    val posterImageView = findViewById(R.id.poster_image)
    return if (posterImageView != null) Pair.create(posterImageView, context.getString(transitionNameId)) else null
}

fun TextView.setTitleAndYear(title: String?, releaseDate: String?) {
    val yearOnly = releaseDate.getYearOnly()
    text = if (yearOnly.isNotEmpty()) "$title ($yearOnly)" else "$title"
}

inline fun ImageView.loadPaletteBitmap(imageUrl: String?, width: Int = 0, height: Int = 0,
                                       crossinline action: ((PaletteBitmap?) -> Unit)) {
    if (imageUrl.isNotNullOrEmpty()) {
        val builder = Glide.with(context)
                .transcodePaletteBitmap(context)
                .load(imageUrl)

        if (width > 0 && height > 0) {
            builder.override(width, height)
        }

        builder.into(object : ImageViewTarget<PaletteBitmap>(this) {
            override fun setResource(paletteBitmap: PaletteBitmap?) {
                setImageBitmap(paletteBitmap?.bitmap)
                action.invoke(paletteBitmap)
            }
        })
    } else {
        Glide.clear(this)
    }
}

fun TextView.applyText(text: String?, viewGone: Boolean = true) {
    if (text.isNotNullOrEmpty()) {
        show()
        this.text = text
    } else {
        hide(viewGone)
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

fun ViewGroup.changeViewGroupTextFont() {
    views.filterIsInstance<TextView>().forEach(TextView::changeTypeface)
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

fun ImageView.loadImageUrl(imageUrl: String?, placeHolder: Int = R.drawable.ic_person_white_80dp) {
    if (imageUrl.isNotNullOrEmpty()) {
        Glide.with(context)
                .load(imageUrl)
                .placeholder(placeHolder)
                .error(placeHolder)
                .into(this)
    } else {
        Glide.clear(this)
    }
}

inline fun View.startCircularRevealAnimation(cx: Int, cy: Int, startRadius: Float, endRadius: Float,
                                             duration: Long = 400L, crossinline animationEnd: () -> Unit) {
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

fun View.onLayoutLaid(action: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            action.invoke()
        }
    })
}