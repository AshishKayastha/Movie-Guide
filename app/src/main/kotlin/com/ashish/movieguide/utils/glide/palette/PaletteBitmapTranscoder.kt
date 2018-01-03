package com.ashish.movieguide.utils.glide.palette

import android.graphics.Bitmap
import android.support.v7.graphics.Palette
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder

/**
 * A [ResourceTranscoder] for generating [Palette]s from [Bitmap]s in the background.
 */
class PaletteBitmapTranscoder(private val bitmapPool: BitmapPool)
    : ResourceTranscoder<Bitmap, PaletteBitmap> {

    override fun transcode(toTranscode: Resource<Bitmap>?, options: Options?): Resource<PaletteBitmap>? {
        val bitmap = toTranscode?.get()
        if (bitmap != null) {
            val palette = Palette.Builder(bitmap).generate()
            val result = PaletteBitmap(bitmap, palette)
            return PaletteBitmapResource(result, bitmapPool)
        }

        return null
    }
}