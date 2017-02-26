package com.ashish.movieguide.utils

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.support.v4.util.SimpleArrayMap
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.TextView
import com.ashish.movieguide.R

/**
 * Created by Ashish on Dec 29.
 */
object FontUtils {

    const val MONTSERRAT_REGULAR = "Montserrat-Regular"
    const val MONTSERRAT_MEDIUM = "Montserrat-Medium"

    private val TYPEFACE_CACHE = SimpleArrayMap<String, Typeface>()

    fun getTypeface(context: Context, fontName: String): Typeface {
        synchronized(TYPEFACE_CACHE) {
            if (!TYPEFACE_CACHE.containsKey(fontName)) {
                val tf = Typeface.createFromAsset(context.assets, "fonts/$fontName.ttf")
                TYPEFACE_CACHE.put(fontName, tf)
                return tf
            }
            return TYPEFACE_CACHE.get(fontName)
        }
    }

    fun setFontStyle(textView: TextView, fontName: String) {
        if (!TextUtils.isEmpty(fontName)) {
            textView.apply {
                paintFlags = textView.paintFlags or Paint.SUBPIXEL_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
                typeface = getTypeface(textView.context, fontName)
            }
        }
    }

    fun applyFont(textView: TextView, attrs: AttributeSet?) {
        if (attrs != null) {
            val ta = textView.context.theme.obtainStyledAttributes(attrs, R.styleable.FontTextView, 0, 0)
            val fontName = ta.getString(R.styleable.FontTextView_fontName)
            setFontStyle(textView, fontName)
            ta.recycle()
        }
    }
}