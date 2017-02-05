package com.ashish.movies.utils.extensions

import android.database.Cursor

fun Cursor.isNullByColumnName(colName: String) = isNull(getColumnIndexOrThrow(colName))

fun Cursor.getIntByColumnName(colName: String) = getInt(getColumnIndexOrThrow(colName))

fun Cursor.getLongByColumnName(colName: String) = getLong(getColumnIndexOrThrow(colName))

fun Cursor.getDoubleByColumnName(colName: String) = getDouble(getColumnIndexOrThrow(colName))

fun Cursor.getStringByColumnName(colName: String): String = getString(getColumnIndexOrThrow(colName))
