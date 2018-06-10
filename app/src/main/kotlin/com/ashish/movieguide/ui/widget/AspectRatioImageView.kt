package com.ashish.movieguide.ui.widget

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import com.ashish.movieguide.R

/**
 * Created by Ashish on Dec 29.
 */
class AspectRatioImageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var heightRatio: Float = 1.0f

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView)
        heightRatio = typedArray.getFloat(R.styleable.AspectRatioImageView_heightRatio, 1.0f)
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        when {
            heightRatio > 0.0f -> {
                val width = MeasureSpec.getSize(widthMeasureSpec)
                val height = (width * heightRatio).toInt()
                setMeasuredDimension(width, height)
            }
            drawable != null -> {
                val w = MeasureSpec.getSize(widthMeasureSpec)
                val h = w * drawable.intrinsicHeight / drawable.intrinsicWidth
                setMeasuredDimension(w, h)
            }
            else -> super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}