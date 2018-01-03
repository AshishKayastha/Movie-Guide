package com.ashish.movieguide.utils.glide

import com.ashish.movieguide.utils.glide.palette.PaletteBitmap
import com.bumptech.glide.TransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory

object PaletteBitmapTransitionOptions : TransitionOptions<PaletteBitmapTransitionOptions, PaletteBitmap>() {

    fun crossFade(): PaletteBitmapTransitionOptions {
        return transition(PaletteBitmapTransitionFactory(
                DrawableCrossFadeFactory.Builder().build()))
    }
}