package com.ashish.movieguide.utils.glide

import android.content.Context
import android.graphics.Bitmap
import com.ashish.movieguide.utils.glide.palette.PaletteBitmap
import com.ashish.movieguide.utils.glide.palette.PaletteBitmapTranscoder
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class GlideAppModule : AppGlideModule() {

    override fun isManifestParsingEnabled() = false

    override fun registerComponents(context: Context, glide: Glide?, registry: Registry?) {
        if (glide != null && registry != null) {
            registry.register(Bitmap::class.java, PaletteBitmap::class.java,
                    PaletteBitmapTranscoder(glide.bitmapPool))
        }
    }
}