package com.ashish.movieguide.utils.transition

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View

object TransitionUtils {

    private val MAX_IMAGE_SIZE = (1024 * 1024).toFloat()

    /**
     * Get a copy of bitmap of given drawable.
     */
    fun createDrawableBitmap(drawable: Drawable): Bitmap? {
        val width = drawable.intrinsicWidth
        val height = drawable.intrinsicHeight
        if (width <= 0 || height <= 0) {
            return null
        }

        val scale = Math.min(1f, MAX_IMAGE_SIZE / (width * height))
        if (drawable is BitmapDrawable && scale == 1f) {
            // return same bitmap if scale down not needed
            return drawable.bitmap
        }

        val bitmapWidth = (width * scale).toInt()
        val bitmapHeight = (height * scale).toInt()
        val bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)

        val existingBounds = drawable.bounds
        val left = existingBounds.left
        val top = existingBounds.top
        val right = existingBounds.right
        val bottom = existingBounds.bottom

        drawable.setBounds(0, 0, bitmapWidth, bitmapHeight)
        drawable.draw(canvas)
        drawable.setBounds(left, top, right, bottom)

        return bitmap
    }

    fun createViewBitmap(sharedElement: View, tempMatrix: Matrix, screenBounds: RectF): Bitmap? {
        var bitmap: Bitmap? = null
        var bitmapWidth = Math.round(screenBounds.width())
        var bitmapHeight = Math.round(screenBounds.height())

        if (bitmapWidth > 0 && bitmapHeight > 0) {
            val scale = Math.min(1f, (MAX_IMAGE_SIZE) / (bitmapWidth * bitmapHeight))
            bitmapWidth = (bitmapWidth * scale).toInt()
            bitmapHeight = (bitmapHeight * scale).toInt()

            tempMatrix.postTranslate(-screenBounds.left, -screenBounds.top)
            tempMatrix.postScale(scale, scale)
            bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888)

            val canvas = Canvas(bitmap)
            canvas.concat(tempMatrix)
            sharedElement.draw(canvas)
        }

        return bitmap
    }
}
