package com.ashish.movieguide.utils.extensions

import com.ashish.movieguide.utils.Constants.DEFAULT_DATE_PATTERN
import com.ashish.movieguide.utils.Constants.MONTH_DAY_YEAR_PATTERN
import timber.log.Timber
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun String?.convertToDate(datePattern: String = DEFAULT_DATE_PATTERN): Date? {
    if (isNotNullOrEmpty()) {
        try {
            val sdf = SimpleDateFormat(datePattern, Locale.ENGLISH)
            return sdf.parse(this)
        } catch (e: ParseException) {
            Timber.e(e)
        }
    }
    return null
}

fun String?.getFormattedDate(
        inputDateFormat: String = DEFAULT_DATE_PATTERN,
        outputDateFormat: String = MONTH_DAY_YEAR_PATTERN
): String? {
    if (isNotNullOrEmpty()) {
        try {
            val sdf = SimpleDateFormat(inputDateFormat, Locale.ENGLISH)
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
    return if (startDate != null && endDate != null) startDate.before(endDate) else true
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

fun String?.getFormattedMediumDate(pattern: String = DEFAULT_DATE_PATTERN): String {
    var formattedDate = ""
    if (isNotNullOrEmpty()) {
        val parsedDate = this!!.getParsedDate(pattern)
        if (parsedDate != null) {
            formattedDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(parsedDate)
        }
    }

    return formattedDate
}