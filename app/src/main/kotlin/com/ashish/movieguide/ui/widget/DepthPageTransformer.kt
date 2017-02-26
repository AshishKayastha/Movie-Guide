package com.ashish.movieguide.ui.widget

import android.support.v4.view.ViewPager.PageTransformer
import android.view.View

class DepthPageTransformer : PageTransformer {

    override fun transformPage(page: View, position: Float) {
        onPreTransform(page, position)
        onTransform(page, position)
    }

    private fun onPreTransform(page: View, position: Float) {
        page.apply {
            rotationX = 0f
            rotationY = 0f
            rotation = 0f
            scaleX = 1f
            scaleY = 1f
            pivotX = 0f
            pivotY = 0f

            translationX = 0f
            translationY = 0f

            alpha = if (position <= -1f || position >= 1f) 0f else 1f
            isEnabled = false
        }
    }

    private fun onTransform(view: View, position: Float) {
        if (position <= 0f) {
            view.apply {
                translationX = 0f
                scaleX = 1f
                scaleY = 1f
            }
        } else if (position <= 1f) {
            val scaleFactor = 0.75f + (1 - 0.75f) * (1 - Math.abs(position))
            view.apply {
                alpha = 1 - position
                pivotY = 0.5f * view.height
                translationX = view.width * -position
                scaleX = scaleFactor
                scaleY = scaleFactor
            }
        }
    }
}