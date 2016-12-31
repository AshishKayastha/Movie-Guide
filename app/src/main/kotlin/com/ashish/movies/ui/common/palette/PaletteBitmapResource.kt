package com.ashish.movies.ui.common.palette

import android.os.Build
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool

/**
 * A [Resource] implementation for [PaletteBitmap].
 */
class PaletteBitmapResource(private val paletteBitmap: PaletteBitmap, private val bitmapPool: BitmapPool)
    : Resource<PaletteBitmap> {

    override fun get() = paletteBitmap

    override fun getSize(): Int {
        val bitmap = paletteBitmap.bitmap
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                return bitmap.allocationByteCount
            } catch (ignored: NullPointerException) {
            }
        }

        return bitmap.height * bitmap.rowBytes
    }

    override fun recycle() {
        val bitmap = paletteBitmap.bitmap
        if (!bitmapPool.put(bitmap)) bitmap.recycle()
    }
}