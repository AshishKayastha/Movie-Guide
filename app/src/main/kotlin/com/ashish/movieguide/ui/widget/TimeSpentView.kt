package com.ashish.movieguide.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import com.ashish.movieguide.R
import com.ashish.movieguide.utils.extensions.applyText
import com.ashish.movieguide.utils.extensions.find
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.extensions.setVisibility

class TimeSpentView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private lateinit var timeSpentCountText: FontTextView
    private lateinit var timeSpentTypeText: FontTextView

    private var timeSpent: String? = null
    private var timeSpentType: String? = null

    init {
        inflate(context, R.layout.view_time_spent, this)
        orientation = VERTICAL
        gravity = Gravity.CENTER

        if (attrs != null) {
            @SuppressLint("Recycle")
            val ta = context.obtainStyledAttributes(attrs, R.styleable.TimeSpentView)
            timeSpentType = ta.getString(R.styleable.TimeSpentView_timeSpentTypeText)
            timeSpent = ta.getString(R.styleable.TimeSpentView_timeSpentTypeCount)
            ta.recycle()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        timeSpentTypeText = find(R.id.timeSpentTypeTitle)
        timeSpentCountText = find(R.id.timeSpentCountText)

        setTimeSpentCount(timeSpent)
        setTimeSpentType(timeSpentType)
    }

    private fun setTimeSpentType(timeSpentType: String?) {
        timeSpentTypeText.applyText(timeSpentType, true)
        setVisibility(timeSpentType.isNotNullOrEmpty())
    }

    fun setTimeSpentCount(timeSpentCount: String?) {
        timeSpentCountText.applyText(timeSpentCount, true)
        setVisibility(timeSpentCount.isNotNullOrEmpty())
    }
}