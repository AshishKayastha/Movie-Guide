package com.ashish.movies.ui.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.annotation.DrawableRes
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.widget.ImageView
import com.ashish.movies.R
import timber.log.Timber

/**
 * Created by Ashish on Jan 29.
 */
class CircleImageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
) : AppCompatImageView(context, attrs, defStyle) {

    companion object {
        private const val DEFAULT_BORDER_WIDTH = 0
        private const val COLOR_DRAWABLE_DIMENSION = 2
        private const val DEFAULT_BORDER_COLOR = Color.TRANSPARENT
    }

    private val borderRect = RectF()
    private val drawableRect = RectF()

    private val bitmapPaint = Paint()
    private val borderPaint = Paint()
    private val shaderMatrix = Matrix()

    private var bitmap: Bitmap? = null
    private var filter: ColorFilter? = null
    private var bitmapShader: BitmapShader? = null

    private var isReady: Boolean = false
    private var setupPending: Boolean = false

    private var drawableRadius: Float = 0f
    private var borderRadius: Float = 0f

    private val borderColor: Int
    private val borderWidth: Int
    private var bitmapWidth: Int = 0
    private var bitmapHeight: Int = 0

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyle, 0)
        borderColor = a.getColor(R.styleable.CircleImageView_civ_borderColor, DEFAULT_BORDER_COLOR)
        borderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_civ_borderWidth, DEFAULT_BORDER_WIDTH)
        a.recycle()
        init()
    }

    private fun init() {
        super.setScaleType(ImageView.ScaleType.FIT_CENTER)
        isReady = true

        if (setupPending) {
            setup()
            setupPending = false
        }
    }

    override fun setScaleType(scaleType: ImageView.ScaleType) {
        if (scaleType != ImageView.ScaleType.FIT_CENTER) {
            throw IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType))
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (drawable == null) return

        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), drawableRadius, bitmapPaint)
        if (borderWidth != 0) {
            canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), borderRadius, borderPaint)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setup()
    }

    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)
        bitmap = bm
        setup()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        bitmap = getBitmapFromDrawable(drawable)
        setup()
    }

    override fun setImageResource(@DrawableRes resId: Int) {
        super.setImageResource(resId)
        bitmap = getBitmapFromDrawable(drawable)
        setup()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        bitmap = getBitmapFromDrawable(drawable)
        setup()
    }

    override fun setColorFilter(cf: ColorFilter) {
        if (cf == filter) return

        filter = cf
        bitmapPaint.colorFilter = filter
        invalidate()
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null) return null

        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        try {
            val bitmap: Bitmap
            if (drawable is ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLOR_DRAWABLE_DIMENSION, COLOR_DRAWABLE_DIMENSION, Bitmap.Config.ARGB_8888)
            } else {
                bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            }

            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        } catch (e: OutOfMemoryError) {
            Timber.e(e)
            return null
        }
    }

    private fun setup() {
        if (!isReady) {
            setupPending = true
            return
        }

        if (bitmap == null) return

        bitmapShader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        bitmapPaint.isAntiAlias = true
        bitmapPaint.shader = bitmapShader

        borderPaint.style = Paint.Style.STROKE
        borderPaint.isAntiAlias = true
        borderPaint.color = borderColor
        borderPaint.strokeWidth = borderWidth.toFloat()

        bitmapHeight = bitmap!!.height
        bitmapWidth = bitmap!!.width

        borderRect.set(0f, 0f, width.toFloat(), height.toFloat())
        borderRadius = Math.min((borderRect.height() - borderWidth) / 2, (borderRect.width() - borderWidth) / 2)

        drawableRect.set(borderRect)
        drawableRadius = Math.min(drawableRect.height() / 2, drawableRect.width() / 2)

        updateShaderMatrix()
        invalidate()
    }

    private fun updateShaderMatrix() {
        shaderMatrix.set(null)

        val scale: Float
        var dx = 0f
        var dy = 0f

        if (bitmapWidth * drawableRect.height() > drawableRect.width() * bitmapHeight) {
            scale = drawableRect.height() / bitmapHeight.toFloat()
            dx = (drawableRect.width() - bitmapWidth * scale) * 0.5f
        } else {
            scale = drawableRect.width() / bitmapWidth.toFloat()
            dy = (drawableRect.height() - bitmapHeight * scale) * 0.5f
        }

        shaderMatrix.setScale(scale, scale)
        shaderMatrix.postTranslate((dx + 0.5f).toInt() + drawableRect.left, (dy + 0.5f).toInt() + drawableRect.top)
        bitmapShader!!.setLocalMatrix(shaderMatrix)
    }
}