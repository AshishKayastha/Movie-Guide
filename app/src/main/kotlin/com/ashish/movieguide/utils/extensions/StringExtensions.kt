package com.ashish.movieguide.utils.extensions

import android.text.Spannable
import android.text.SpannableString
import android.text.style.TypefaceSpan
import com.ashish.movieguide.utils.Constants.BACKDROP_URL_PREFIX
import com.ashish.movieguide.utils.Constants.ORIGINAL_IMAGE_URL_PREFIX
import com.ashish.movieguide.utils.Constants.POSTER_URL_PREFIX
import com.ashish.movieguide.utils.Constants.PROFILE_URL_PREFIX
import com.ashish.movieguide.utils.Constants.STILL_URL_PREFIX
import java.text.NumberFormat

/**
 * Created by Ashish on Jan 03.
 */
fun CharSequence?.isNotNullOrEmpty() = !this.isNullOrEmpty() && this != "null"

fun String?.getYearOnly() = if (isNotNullOrEmpty()) this!!.slice(0..3) else ""

fun Int?.getFormattedCurrency(): String {
    if (this != null && this > 0L) {
        return String.format("$%s", NumberFormat.getNumberInstance().format(this))
    }
    return ""
}

fun Int?.getFormattedRuntime(): String {
    if (this != null && this > 0) {
        val hours = this / 60
        val minutes = this % 60

        if (hours > 0) {
            if (minutes > 0) {
                return String.format("%dh %dm", hours, minutes)
            } else {
                return String.format("%dh", hours)
            }
        } else {
            return String.format("%dm", minutes)
        }
    }

    return ""
}

inline fun <T> List<T>?.convertListToCommaSeparatedText(crossinline func: (T) -> String): String {
    var formattedGenre = ""
    if (this != null && isNotEmpty()) {
        formattedGenre = joinToString { func(it) }
    }
    return formattedGenre
}

fun String?.getBackdropUrl() = getImageUrl(BACKDROP_URL_PREFIX)

fun String?.getPosterUrl() = getImageUrl(POSTER_URL_PREFIX)

fun String?.getProfileUrl() = getImageUrl(PROFILE_URL_PREFIX)

fun String?.getStillImageUrl() = getImageUrl(STILL_URL_PREFIX)

fun String?.getOriginalImageUrl() = getImageUrl(ORIGINAL_IMAGE_URL_PREFIX)

fun String?.getImageUrl(urlPrefix: String) = if (isNotNullOrEmpty()) urlPrefix + this else ""

fun CharSequence?.getTextWithCustomTypeface(typefaceSpan: TypefaceSpan): SpannableString? {
    if (isNotNullOrEmpty()) {
        val spannableString = SpannableString(this)
        spannableString.setSpan(typefaceSpan, 0, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    } else {
        return null
    }
}