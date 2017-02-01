package com.ashish.movies.utils.extensions

import android.annotation.SuppressLint
import com.ashish.movies.utils.Constants.DEFAULT_DATE_PATTERN
import com.ashish.movies.utils.Constants.MONTH_DAY_YEAR_PATTERN
import timber.log.Timber
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Ashish Kayastha on Jan 26.
 */
@SuppressLint("SimpleDateFormat")
fun String?.convertToDate(datePattern: String = DEFAULT_DATE_PATTERN): Date? {
    if (isNotNullOrEmpty()) {
        try {
            val sdf = SimpleDateFormat(datePattern)
            return sdf.parse(this)
        } catch (e: ParseException) {
            Timber.e(e)
        }
    }
    return null
}

@SuppressLint("SimpleDateFormat")
fun String?.getFormattedDate(inputDateFormat: String = DEFAULT_DATE_PATTERN,
                             outputDateFormat: String = MONTH_DAY_YEAR_PATTERN): String? {
    if (isNotNullOrEmpty()) {
        try {
            val sdf = SimpleDateFormat(inputDateFormat)
            val date = sdf.parse(this)
            sdf.applyPattern(outputDateFormat)
            return sdf.format(date)
        } catch (e: ParseException) {
            Timber.e(e)
        }
    }
    return null
}

fun isValidDate(startDateString: String?, endDateString: String?): Boolean {
    val startDate = startDateString.convertToDate()
    val endDate = endDateString.convertToDate()
    if (startDate != null && endDate != null) {
        return startDate.before(endDate)
    }

    return true
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