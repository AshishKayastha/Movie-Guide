package com.ashish.movieguide.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.RectF
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import com.ashish.movieguide.R
import com.ashish.movieguide.utils.FontUtils
import com.ashish.movieguide.utils.extensions.dpToPx
import com.ashish.movieguide.utils.extensions.getColorCompat
import com.ashish.movieguide.utils.extensions.spToPx

class CircularTextDrawable(context: Context, private val text: String) : ShapeDrawable(OvalShape()) {

    private val borderWidth = 2f.dpToPx()
    private val textPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        textPaint.run {
            color = Color.WHITE
            style = Paint.Style.FILL
            textSize = 18f.spToPx()
            textAlign = Paint.Align.CENTER
            typeface = FontUtils.getTypeface(context, FontUtils.MONTSERRAT_MEDIUM)
        }

        borderPaint.run {
            color = Color.WHITE
            style = Paint.Style.STROKE
            strokeWidth = borderWidth
        }

        paint.color = context.getColorCompat(R.color.colorPrimary)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        drawBorder(canvas)

        val count = canvas.save()
        canvas.translate(bounds.left.toFloat(), bounds.top.toFloat())

        val x = (bounds.width() / 2).toFloat()
        val y = bounds.height() / 2 - (textPaint.descent() + textPaint.ascent()) / 2
        canvas.drawText(text, x, y, textPaint)

        canvas.restoreToCount(count)
    }

    private fun drawBorder(canvas: Canvas) {
        val rect = RectF(bounds)
        rect.inset(borderWidth / 2, borderWidth / 2)
        canvas.drawOval(rect, borderPaint)
    }

    override fun setAlpha(alpha: Int) {
        textPaint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        textPaint.colorFilter = colorFilter
    }

    override fun getOpacity() = PixelFormat.TRANSLUCENT

    override fun getIntrinsicWidth() = -1

    override fun getIntrinsicHeight() = -1
}