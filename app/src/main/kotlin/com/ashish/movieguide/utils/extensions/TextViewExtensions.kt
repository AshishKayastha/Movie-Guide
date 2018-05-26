package com.ashish.movieguide.utils.extensions

import android.widget.TextView
import com.ashish.movieguide.utils.FontUtils

fun TextView.changeTypeface(boldFont: Boolean = false) {
    val fontName = if (boldFont) FontUtils.MONTSERRAT_MEDIUM else FontUtils.MONTSERRAT_REGULAR
    typeface = FontUtils.getTypeface(context, fontName)
}

fun TextView.applyText(text: String?, viewGone: Boolean = true) {
    if (text.isNotNullOrEmpty()) {
        show()
        this.text = text
    } else {
        hide(viewGone)
    }
}

fun TextView.setTitleAndYear(title: String?, releaseDate: String?) {
    val yearOnly = releaseDate.getYearOnly()
    text = if (yearOnly.isNotEmpty()) "$title ($yearOnly)" else "$title"
}

fun TextView.animateTextColorChange(startColor: Int, endColor: Int) {
    animateColorChange(startColor, endColor, onAnimationUpdate = { setTextColor(it) })
}