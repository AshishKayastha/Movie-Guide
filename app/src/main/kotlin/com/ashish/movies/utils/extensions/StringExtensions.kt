package com.ashish.movies.utils.extensions

import com.ashish.movies.utils.ApiConstants.BACKDROP_W1280_URL_PREFIX
import com.ashish.movies.utils.ApiConstants.ORIGINAL_IMAGE_URL_PREFIX
import com.ashish.movies.utils.ApiConstants.POSTER_W500_URL_PREFIX
import com.ashish.movies.utils.Constants.DEFAULT_DATE_PATTERN
import timber.log.Timber
import java.text.DateFormat
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Ashish on Jan 03.
 */
fun String?.isNotNullOrEmpty() = !this.isNullOrEmpty() && this != "null"

fun String?.getYearOnly(): String {
    return if (isNotNullOrEmpty()) this!!.slice(0..3) else ""
}

fun String.getParsedDate(pattern: String = DEFAULT_DATE_PATTERN): Date? {
    try {
        val sdf = SimpleDateFormat(pattern, Locale.ENGLISH)
        sdf.timeZone = TimeZone.getDefault()
        return sdf.parse(this)
    } catch (e: ParseException) {
        Timber.e(e)
    }

    return null
}

fun Int?.getFormattedNumber(): String {
    if (this != null && this > 0L) {
        return String.format("$%s", NumberFormat.getNumberInstance(Locale.US).format(this))
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

fun String?.getFormattedMediumDate(): String {
    var formattedReleaseDate = ""
    if (isNotNullOrEmpty()) {
        val parsedDate = this!!.getParsedDate()
        if (parsedDate != null) {
            formattedReleaseDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(parsedDate)
        }
    }

    return formattedReleaseDate
}

inline fun <reified T> List<T>?.convertListToCommaSeparatedText(crossinline func: (T) -> String): String {
    var formattedGenre = ""
    if (this != null && isNotEmpty()) {
        formattedGenre = joinToString { func(it) }
    }
    return formattedGenre
}

fun String?.getBackdropUrl() = getImageUrl(BACKDROP_W1280_URL_PREFIX)

fun String?.getPosterUrl() = getImageUrl(POSTER_W500_URL_PREFIX)

fun String?.getOriginalImageUrl() = getImageUrl(ORIGINAL_IMAGE_URL_PREFIX)

fun String?.getImageUrl(urlPrefix: String): String {
    return if (this.isNotNullOrEmpty()) urlPrefix + this else ""
}