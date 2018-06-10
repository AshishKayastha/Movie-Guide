package com.ashish.movieguide.utils.extensions

import android.text.Spannable
import android.text.SpannableString
import android.text.style.TypefaceSpan
import com.ashish.movieguide.utils.TMDbConstants.BACKDROP_URL_PREFIX
import com.ashish.movieguide.utils.TMDbConstants.ORIGINAL_IMAGE_URL_PREFIX
import com.ashish.movieguide.utils.TMDbConstants.POSTER_URL_PREFIX
import com.ashish.movieguide.utils.TMDbConstants.PROFILE_URL_PREFIX
import com.ashish.movieguide.utils.TMDbConstants.STILL_URL_PREFIX
import java.text.NumberFormat

fun CharSequence?.isNotNullOrEmpty() = !isNullOrEmpty() && this != "null"

fun String?.getYearOnly(): String = if (isNotNullOrEmpty()) this!!.slice(0..3) else ""

fun Int?.getFormattedCurrency(): String {
    return if (this != null && this > 0L) {
        String.format("$%s", NumberFormat.getNumberInstance().format(this))
    } else ""
}

fun Int?.getFormattedRuntime(): String {
    return if (this != null && this > 0) {
        val hours = this / 60
        val minutes = this % 60
        if (hours > 0) {
            if (minutes > 0) {
                String.format("%dh %dm", hours, minutes)
            } else {
                String.format("%dh", hours)
            }
        } else {
            String.format("%dm", minutes)
        }
    } else ""
}

inline fun <T> List<T>?.convertListToCommaSeparatedText(crossinline func: (T) -> String): String {
    return if (isNotNullOrEmpty()) this!!.joinToString { func(it) } else ""
}

fun String?.getBackdropUrl(): String = getImageUrl(BACKDROP_URL_PREFIX)

fun String?.getPosterUrl(): String = getImageUrl(POSTER_URL_PREFIX)

fun String?.getProfileUrl(): String = getImageUrl(PROFILE_URL_PREFIX)

fun String?.getStillImageUrl(): String = getImageUrl(STILL_URL_PREFIX)

fun String?.getOriginalImageUrl(): String = getImageUrl(ORIGINAL_IMAGE_URL_PREFIX)

fun String.convertToOriginalImageUrl(): String {
    return when {
        contains(POSTER_URL_PREFIX) -> replace(POSTER_URL_PREFIX, ORIGINAL_IMAGE_URL_PREFIX)
        contains(BACKDROP_URL_PREFIX) -> replace(BACKDROP_URL_PREFIX, ORIGINAL_IMAGE_URL_PREFIX)
        else -> this
    }
}

fun String?.getImageUrl(urlPrefix: String): String = if (isNotNullOrEmpty()) urlPrefix + this else ""

fun CharSequence?.getTextWithCustomTypeface(typefaceSpan: TypefaceSpan): SpannableString? {
    return if (isNotNullOrEmpty()) {
        val spannableString = SpannableString(this)
        spannableString.setSpan(typefaceSpan, 0, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString
    } else null
}