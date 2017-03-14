package com.ashish.movieguide.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.ashish.movieguide.R
import com.ashish.movieguide.ui.animation.ProgressBarAnimation
import com.ashish.movieguide.utils.extensions.applyText
import com.ashish.movieguide.utils.extensions.find
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.extensions.setVisibility

class RatingCountLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private lateinit var progressBar: ProgressBar
    private lateinit var numOfRatingsText: FontTextView
    private lateinit var ratingsCountText: FontTextView

    private var progress: Int = 0
    private var maxProgress: Int = 0
    private var numOfRatings: String? = null
    private var ratingsCount: String? = null

    private var starCount: String

    init {
        inflate(context, R.layout.view_rating_count, this)
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL

        starCount = context.getString(R.string.star_count)

        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.RatingCountLayout)
            ratingsCount = ta.getString(R.styleable.RatingCountLayout_ratingCount)
            numOfRatings = ta.getString(R.styleable.RatingCountLayout_numRating)
            maxProgress = ta.getInt(R.styleable.RatingCountLayout_android_max, 0)
            progress = ta.getInt(R.styleable.RatingCountLayout_android_progress, 0)
            ta.recycle()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        progressBar = find<ProgressBar>(R.id.rating_count_progress)
        ratingsCountText = find<FontTextView>(R.id.rating_count_text)
        numOfRatingsText = find<FontTextView>(R.id.num_rating_text)

        setRatingCount(ratingsCount)
        setNumOfRatings(numOfRatings)
        setRatingProgress(maxProgress, progress)
    }

    fun setNumOfRatings(numOfRating: String?) {
        if (numOfRating.isNotNullOrEmpty()) {
            val count = Integer.parseInt(numOfRating)
            numOfRatingsText.text = String.format(starCount, count)
        }
    }

    fun setRatingCount(ratingCount: String?) {
        ratingsCountText.applyText(ratingCount, true)
        setVisibility(ratingCount.isNotNullOrEmpty())
    }

    fun setRatingProgress(maxProgress: Int, progress: Int) {
        progressBar.max = maxProgress
        val anim = ProgressBarAnimation(progressBar, 0, progress)
        anim.duration = 1000L
        progressBar.startAnimation(anim)
    }
}