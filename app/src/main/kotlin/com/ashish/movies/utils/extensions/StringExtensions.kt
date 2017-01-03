package com.ashish.movies.utils.extensions

import android.content.Context
import android.text.format.DateFormat
import com.ashish.movies.data.models.Genre
import com.ashish.movies.utils.Constants.DEFAULT_DATE_PATTERN
import com.ashish.movies.utils.Constants.NOT_AVAILABLE
import timber.log.Timber
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Ashish on Jan 03.
 */
fun String?.isNotNullOrEmpty() = !this.isNullOrEmpty()

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

    return NOT_AVAILABLE
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

    return NOT_AVAILABLE
}

fun String?.getFormattedReleaseDate(context: Context): String {
    var formattedReleaseDate = NOT_AVAILABLE
    if (this.isNotNullOrEmpty()) {
        val parsedDate = this!!.getParsedDate()
        if (parsedDate != null) {
            formattedReleaseDate = DateFormat.getMediumDateFormat(context).format(parsedDate)
        }
    }

    return formattedReleaseDate
}

fun List<Genre>?.getFormattedGenres(): String {
    var formattedGenre = NOT_AVAILABLE
    if (this != null && isNotEmpty()) {
        formattedGenre = joinToString { genre -> genre.name.toString() }
    }
    return formattedGenre
}