package com.ashish.movieguide.utils.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.support.annotation.ColorInt
import android.support.v7.view.menu.ActionMenuItemView
import android.text.style.TypefaceSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import com.ashish.movieguide.R
import java.util.ArrayList

/**
 * Created by Ashish on Feb 08.
 */
val Menu.menus: List<MenuItem>
    get() = (0 until size()).map { getItem(it) }

fun Menu.changeMenuFont(typefaceSpan: TypefaceSpan) {
    menus.filterNotNull()
            .forEach { menuItem -> menuItem.title = menuItem.title.getTextWithCustomTypeface(typefaceSpan) }
}

fun Menu.tint(@ColorInt color: Int) = menus.forEach { it.tint(color) }

fun MenuItem?.tint(@ColorInt color: Int) = this?.icon.tint(color)

@SuppressLint("PrivateResource")
fun Activity.setOverflowMenuColor(color: Int) {
    val decorView = window.decorView as ViewGroup
    val overflowDescription = getString(R.string.abc_action_menu_overflow_description)

    decorView.onLayoutLaid {
        val views = ArrayList<View>()
        decorView.findViewsWithText(views, overflowDescription, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION)
        if (views.isEmpty()) return@onLayoutLaid

        val overflow = views[0] as ImageView
        overflow.setColorFilter(color)
    }
}

fun Menu.setFavoriteIcon(isFavorite: Boolean?) {
    val favItem = findItem(R.id.action_favorite)
    favItem.apply {
        isVisible = true
        if (isFavorite == true) {
            setIcon(R.drawable.ic_favorite_white_24dp)
        } else {
            setIcon(R.drawable.ic_favorite_border_white_24dp)
        }
    }
}

fun ActionMenuItemView.startFavoriteAnimation(isFavorite: Boolean) {
    val animatorSet = AnimatorSet()
    val animDuration = 350L
    val overshootInterpolator = OvershootInterpolator()

    val rotationAnim = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f)
    rotationAnim.duration = animDuration
    rotationAnim.interpolator = AccelerateInterpolator()

    val bounceAnimX = ObjectAnimator.ofFloat(this, "scaleX", 0.3f, 1f)
    bounceAnimX.duration = animDuration
    bounceAnimX.interpolator = overshootInterpolator

    val bounceAnimY = ObjectAnimator.ofFloat(this, "scaleY", 0.3f, 1f)
    bounceAnimY.duration = animDuration
    bounceAnimY.interpolator = overshootInterpolator
    bounceAnimY.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animator: Animator) {
            if (isFavorite) {
                setIcon(context.getDrawableCompat(R.drawable.ic_favorite_white_24dp))
            } else {
                setIcon(context.getDrawableCompat(R.drawable.ic_favorite_border_white_24dp))
            }
        }
    })

    animatorSet.play(rotationAnim)
    animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim)
    animatorSet.start()
}

fun Menu.changeMenuItemTitle(menuItemId: Int, titleId: Int) {
    val menuItem = findItem(menuItemId)
    menuItem?.apply {
        isVisible = true
        setTitle(titleId)
    }
}