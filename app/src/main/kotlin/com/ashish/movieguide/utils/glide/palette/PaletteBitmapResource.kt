package com.ashish.movieguide.utils.glide.palette

import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.util.Util

/**
 * A [Resource] implementation for [PaletteBitmap].
 */
class PaletteBitmapResource(
        private val paletteBitmap: PaletteBitmap,
        private val bitmapPool: BitmapPool
) : Resource<PaletteBitmap> {

    override fun get() = paletteBitmap

    override fun getSize() = Util.getBitmapByteSize(paletteBitmap.bitmap)

    override fun recycle() = bitmapPool.put(paletteBitmap.bitmap)

    override fun getResourceClass() = PaletteBitmap::class.java
}