package com.ashish.movies.utils.extensions

import com.ashish.movies.utils.Constants.DEFAULT_DATE_PATTERN
import com.ashish.movies.utils.Constants.MONTH_DAY_YEAR_PATTERN
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Ashish Kayastha on Jan 26.
 */
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