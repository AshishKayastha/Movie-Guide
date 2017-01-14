package com.ashish.movies.ui.widget

import android.support.v4.view.ViewPager.PageTransformer
import android.view.View

class DepthPageTransformer : PageTransformer {

    override fun transformPage(page: View, position: Float) {
        onPreTransform(page, position)
        onTransform(page, position)
    }

    private fun onPreTransform(page: View, position: Float) {
        page.rotationX = 0f
        page.rotationY = 0f
        page.rotation = 0f
        page.scaleX = 1f
        page.scaleY = 1f
        page.pivotX = 0f
        page.pivotY = 0f

        page.translationX = 0f
        page.translationY = 0f

        page.alpha = if (position <= -1f || position >= 1f) 0f else 1f
        page.isEnabled = false
    }

    private fun onTransform(view: View, position: Float) {
        if (position <= 0f) {
            view.translationX = 0f
            view.scaleX = 1f
            view.scaleY = 1f
        } else if (position <= 1f) {
            val scaleFactor = 0.75f + (1 - 0.75f) * (1 - Math.abs(position))
            view.alpha = 1 - position
            view.pivotY = 0.5f * view.height
            view.translationX = view.width * -position
            view.scaleX = scaleFactor
            view.scaleY = scaleFactor
        }
    }
}