package com.ashish.movieguide.ui.widget

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
            val ta = context.obtainStyledAttributes(attrs, R.styleable.TimeSpentView)
            timeSpentType = ta.getString(R.styleable.TimeSpentView_timeSpentTypeText)
            timeSpent = ta.getString(R.styleable.TimeSpentView_timeSpentTypeCount)
            ta.recycle()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        timeSpentTypeText = find<FontTextView>(R.id.time_spent_type_title)
        timeSpentCountText = find<FontTextView>(R.id.time_spent_count_text)

        setTimeSpentCount(timeSpent)
        setTimeSpentType(timeSpentType)
    }

    fun setTimeSpentType(timeSpentType: String?) {
        timeSpentTypeText.applyText(timeSpentType, true)
        setVisibility(timeSpentType.isNotNullOrEmpty())
    }

    fun setTimeSpentCount(timeSpentCount: String?) {
        timeSpentCountText.applyText(timeSpentCount, true)
        setVisibility(timeSpentCount.isNotNullOrEmpty())
    }
}