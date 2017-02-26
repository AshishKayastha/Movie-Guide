package com.ashish.movieguide.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.v7.widget.TintTypedArray
import android.util.AttributeSet
import android.widget.FrameLayout
import com.ashish.movieguide.R
import com.ashish.movieguide.utils.FontUtils
import com.ashish.movieguide.utils.extensions.show
import com.ashish.movieguide.utils.extensions.spToPx

/**
 * LabelLayout provides a label at the corner to display text
 */
class LabelLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val labelRect = Rect()
    private val textBounds = Rect()
    private val backgroundRect = Rect()

    private val bisectorPath = Path()
    private val textPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var labelText: String
    private val labelGravity: Gravity
    private var labelBackground: Drawable

    private val labelHeight: Int
    private val labelTextSize: Int
    private val labelDistance: Int
    private var labelTextColor: Int

    init {
        setWillNotDraw(false)

        val ta = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.LabelLayout)

        labelBackground = ta.getDrawable(R.styleable.LabelLayout_labelBackground)
        labelHeight = ta.getDimensionPixelSize(R.styleable.LabelLayout_labelHeight, 0)
        labelDistance = ta.getDimensionPixelSize(R.styleable.LabelLayout_labelDistance, 0)
        labelGravity = Gravity.values()[ta.getInt(R.styleable.LabelLayout_labelGravity, Gravity.TOP_LEFT.ordinal)]

        labelText = ta.getString(R.styleable.LabelLayout_android_text)
        labelTextColor = ta.getColor(R.styleable.LabelLayout_labelTextColor, Color.WHITE)
        labelTextSize = ta.getDimensionPixelSize(R.styleable.LabelLayout_android_textSize, 14f.spToPx().toInt())
        ta.recycle()

        textPaint.apply {
            isDither = true
            isAntiAlias = true
            strokeCap = Paint.Cap.SQUARE
            strokeJoin = Paint.Join.ROUND
            typeface = FontUtils.getTypeface(context, FontUtils.MONTSERRAT_REGULAR)
        }
    }

    override fun onDrawForeground(canvas: Canvas) {
        super.onDrawForeground(canvas)

        // Draw background
        val centerCoordinate = calculateCenterCoordinate(labelDistance, labelHeight, labelGravity)
        val labelHalfWidth = calculateWidth(labelDistance, labelHeight) / 2
        val labelHalfHeight = labelHeight / 2

        labelRect.apply {
            left = centerCoordinate[0] - labelHalfWidth
            right = centerCoordinate[0] + labelHalfWidth
            top = centerCoordinate[1] - labelHalfHeight
            bottom = centerCoordinate[1] + labelHalfHeight
        }

        labelBackground.bounds = calculateBackgroundBounds(labelBackground, labelRect)

        canvas.save()
        canvas.rotate(calculateRotateDegree(labelGravity), centerCoordinate[0].toFloat(), centerCoordinate[1].toFloat())
        labelBackground.draw(canvas)
        canvas.restore()

        // Draw text
        bisectorPath.reset()
        val bisectorCoordinates = calculateBisectorCoordinates(labelDistance, labelHeight, labelGravity)
        bisectorPath.moveTo(bisectorCoordinates[0].toFloat(), bisectorCoordinates[1].toFloat())
        bisectorPath.lineTo(bisectorCoordinates[2].toFloat(), bisectorCoordinates[3].toFloat())

        textPaint.color = labelTextColor
        textPaint.textSize = labelTextSize.toFloat()

        val offsets = calculateTextOffsets(labelText, textPaint, labelDistance, labelHeight, labelGravity)
        canvas.drawTextOnPath(labelText, bisectorPath, offsets[0], offsets[1], textPaint)
    }

    fun setLabelBackground(labelBackground: Drawable) {
        this.labelBackground = labelBackground
        invalidate()
    }

    fun setLabelText(labelText: String) {
        show()
        this.labelText = labelText
        invalidate()
    }

    fun setLabelTextColor(@ColorInt labelTextColor: Int) {
        this.labelTextColor = labelTextColor
        invalidate()
    }

    // Calculate the absolute position of point intersecting between canvas edge and bisector
    private fun calculateBisectorIntersectAbsolutePosition(distance: Int, height: Int)
            = (Math.sqrt(2.0) * (distance + height / 2)).toInt()

    // Calculate the starting and ending points coordinates of bisector line
    private fun calculateBisectorCoordinates(distance: Int, height: Int, gravity: Gravity): IntArray {
        val results = IntArray(4)
        val bisectorIntersectAbsolutePosition = calculateBisectorIntersectAbsolutePosition(distance, height)

        when (gravity) {
            Gravity.TOP_RIGHT -> {
                results[0] = measuredWidth - bisectorIntersectAbsolutePosition
                results[1] = 0
                results[2] = measuredWidth
                results[3] = bisectorIntersectAbsolutePosition
            }

            Gravity.BOTTOM_RIGHT -> {
                results[0] = measuredWidth - bisectorIntersectAbsolutePosition
                results[1] = measuredHeight
                results[2] = measuredWidth
                results[3] = measuredHeight - bisectorIntersectAbsolutePosition
            }

            Gravity.BOTTOM_LEFT -> {
                results[0] = 0
                results[1] = measuredHeight - bisectorIntersectAbsolutePosition
                results[2] = bisectorIntersectAbsolutePosition
                results[3] = measuredHeight
            }

            else -> {
                results[0] = 0
                results[1] = bisectorIntersectAbsolutePosition
                results[2] = bisectorIntersectAbsolutePosition
                results[3] = 0
            }
        }

        return results
    }

    // Calculate text horizontal and vertical offset
    private fun calculateTextOffsets(text: String, paint: Paint, distance: Int, height: Int,
                                     gravity: Gravity): FloatArray {
        val offsets = FloatArray(2)
        paint.getTextBounds(text, 0, text.length, textBounds)

        offsets[0] = (calculateBisectorIntersectAbsolutePosition(distance, height) / Math.sqrt(2.0) - textBounds.width() / 2.0).toFloat()

        if (distance >= height) {
            offsets[1] = textBounds.height() * 0.5f
        } else {
            if (gravity == Gravity.TOP_LEFT || gravity == Gravity.TOP_RIGHT) {
                offsets[1] = textBounds.height() * (0.5f + (height - distance) / height.toFloat() * 0.5f)
            } else {
                offsets[1] = textBounds.height() * (0.5f - (height - distance) / height.toFloat() * 0.5f)
            }
        }

        return offsets
    }

    private fun calculateCenterCoordinate(distance: Int, height: Int, gravity: Gravity): IntArray {
        val results = IntArray(2)
        val centerAbsolutePosition = calculateCenterAbsolutePosition(distance, height)

        when (gravity) {
            Gravity.TOP_RIGHT -> {
                results[0] = measuredWidth - centerAbsolutePosition
                results[1] = centerAbsolutePosition
            }

            Gravity.BOTTOM_RIGHT -> {
                results[0] = measuredWidth - centerAbsolutePosition
                results[1] = measuredHeight - centerAbsolutePosition
            }

            Gravity.BOTTOM_LEFT -> {
                results[0] = centerAbsolutePosition
                results[1] = measuredHeight - centerAbsolutePosition
            }

            else -> {
                results[0] = centerAbsolutePosition
                results[1] = centerAbsolutePosition
            }
        }

        return results
    }

    private fun calculateCenterAbsolutePosition(distance: Int, height: Int): Int {
        return ((distance + height / 2) / Math.sqrt(2.0)).toInt()
    }

    private fun calculateWidth(distance: Int, height: Int): Int {
        return 2 * (distance + height)
    }

    private fun calculateBackgroundBounds(drawable: Drawable, labelRect: Rect): Rect {
        if (drawable is ColorDrawable) {
            backgroundRect.set(labelRect)
        } else {
            val intrinsicWidth = drawable.intrinsicWidth
            val intrinsicHeight = drawable.intrinsicHeight

            val labelRectWidth = labelRect.width()
            val labelRectHeight = labelRect.height()

            if (intrinsicWidth <= labelRectWidth && intrinsicHeight <= labelRectHeight) {
                // No need to scale
                backgroundRect.apply {
                    left = labelRect.centerX() - intrinsicWidth / 2
                    top = labelRect.centerY() - intrinsicHeight / 2
                    right = labelRect.centerX() + intrinsicWidth / 2
                    bottom = labelRect.centerY() + intrinsicHeight / 2
                }
            } else {
                // Need to scale
                val width: Int
                val height: Int
                val ratio = intrinsicWidth / intrinsicHeight

                if (intrinsicWidth / intrinsicHeight >= labelRectWidth / labelRectHeight) {
                    // Scale to fill width
                    width = labelRectWidth
                    height = labelRectWidth / ratio
                } else {
                    // Scale to fill height
                    width = labelRectHeight * ratio
                    height = labelRectHeight
                }

                backgroundRect.apply {
                    with(labelRect) {
                        left = centerX() - width / 2
                        top = centerY() - height / 2
                        right = centerX() + width / 2
                        bottom = centerY() + height / 2
                    }
                }
            }
        }

        return backgroundRect
    }

    private fun calculateRotateDegree(gravity: Gravity): Float {
        return when (gravity) {
            Gravity.TOP_RIGHT, Gravity.BOTTOM_LEFT -> 45f
            else -> -45f
        }
    }

    private enum class Gravity {
        TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT
    }
}