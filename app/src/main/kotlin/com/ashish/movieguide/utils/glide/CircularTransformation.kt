package com.ashish.movieguide.utils.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Shader
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapResource
import java.security.MessageDigest

class CircularTransformation(private val bitmapPool: BitmapPool) : Transformation<Bitmap> {

    override fun transform(context: Context?, resource: Resource<Bitmap>,
                           outWidth: Int,
                           outHeight: Int): Resource<Bitmap> {
        val sourceBitmap = resource.get()
        val size = Math.min(sourceBitmap.width, sourceBitmap.height)

        val width = (sourceBitmap.width - size) / 2
        val height = (sourceBitmap.height - size) / 2

        val bitmap: Bitmap = bitmapPool.get(size, size, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        val shader = BitmapShader(sourceBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        if (width != 0 || height != 0) {
            val matrix = Matrix()
            matrix.setTranslate(-width.toFloat(), -height.toFloat())
            shader.setLocalMatrix(matrix)
        }

        paint.shader = shader

        val radius = size / 2f
        canvas.drawCircle(radius, radius, radius, paint)
        return BitmapResource.obtain(bitmap, bitmapPool)!!
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest?) {}
}