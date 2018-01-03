package com.ashish.movieguide.ui.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v7.content.res.AppCompatResources
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import com.ashish.movieguide.R
import com.ashish.movieguide.utils.extensions.applyText
import com.ashish.movieguide.utils.extensions.find
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.extensions.setVisibility

class RatingView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private lateinit var ratingImage: ImageView
    private lateinit var ratingText: FontTextView

    private var text: String? = null
    private var drawable: Drawable? = null

    init {
        inflate(context, R.layout.view_rating, this)

        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.RatingView)
            text = ta.getString(R.styleable.RatingView_android_text)
            drawable = ta.getDrawable(R.styleable.RatingView_android_src)
            ta.recycle()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        ratingImage = find(R.id.ratingImage)
        ratingText = find(R.id.ratingText)

        if (text.isNotNullOrEmpty()) setText(text)
        if (drawable != null) ratingImage.setImageDrawable(drawable)
    }

    fun setText(text: String?) {
        ratingText.applyText(text, true)
        setVisibility(text.isNotNullOrEmpty())
    }

    fun setDrawableResource(@DrawableRes drawableId: Int) {
        ratingImage.setImageDrawable(AppCompatResources.getDrawable(context, drawableId))
    }
}