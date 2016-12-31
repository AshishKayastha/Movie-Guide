package com.ashish.movies.ui.common.palette

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.graphics.Palette

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder

/**
 * A [ResourceTranscoder] for generating [Palette]s from [Bitmap]s in the background.
 */
class PaletteBitmapTranscoder(context: Context) : ResourceTranscoder<Bitmap, PaletteBitmap> {

    private val bitmapPool: BitmapPool

    init {
        this.bitmapPool = Glide.get(context).bitmapPool
    }

    override fun transcode(toTranscode: Resource<Bitmap>): Resource<PaletteBitmap> {
        val bitmap = toTranscode.get()
        val palette = Palette.Builder(bitmap).generate()
        val result = PaletteBitmap(bitmap, palette)
        return PaletteBitmapResource(result, bitmapPool)
    }

    override fun getId(): String = PaletteBitmapTranscoder::class.java.name
}