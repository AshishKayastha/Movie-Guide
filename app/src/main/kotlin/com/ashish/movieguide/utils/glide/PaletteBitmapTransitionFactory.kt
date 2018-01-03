package com.ashish.movieguide.utils.glide

import android.graphics.drawable.Drawable
import com.ashish.movieguide.utils.glide.palette.PaletteBitmap
import com.bumptech.glide.request.transition.BitmapContainerTransitionFactory
import com.bumptech.glide.request.transition.TransitionFactory

class PaletteBitmapTransitionFactory(realFactory: TransitionFactory<Drawable>)
    : BitmapContainerTransitionFactory<PaletteBitmap>(realFactory) {

    override fun getBitmap(paletteBitmap: PaletteBitmap) = paletteBitmap.bitmap
}