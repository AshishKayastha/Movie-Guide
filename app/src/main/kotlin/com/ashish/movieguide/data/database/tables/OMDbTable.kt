package com.ashish.movieguide.data.database.tables

/**
 * Created by Ashish on Feb 06.
 */
object OMDbTable : DatabaseTable {

    override val TABLE_NAME = "omdb_contents"

    const val COL_IMDB_ID = "imdb_id"
    const val COL_MEDIA_ID = "media_id"
    const val COL_RATED = "rated"
    const val COL_COUNTRY = "country"
    const val COL_AWARDS = "awards"
    const val COL_LANGUAGE = "language"
    const val COL_PRODUCTION = "production"
    const val COL_METASCORE = "metascore"
    const val COL_IMDB_RATING = "imdb_rating"
    const val COL_IMDB_VOTES = "imdb_votes"
    const val COL_TOMATO_METER = "tomato_meter"
    const val COL_TOMATO_IMAGE = "tomato_image"
    const val COL_TOMATO_RATING = "tomato_rating"
    const val COL_TOMATO_URL = "tomato_url"
    const val COL_TOMATO_USER_METER = "tomato_user_meter"
    const val COL_TOMATO_USER_RATING = "tomato_user_rating"

    override val CREATE_TABLE = "CREATE TABLE $TABLE_NAME(" +
            "$COL_IMDB_ID TEXT NOT NULL PRIMARY KEY, " +
            "$COL_MEDIA_ID INTEGER NOT NULL, " +
            "$COL_RATED TEXT, " +
            "$COL_COUNTRY TEXT, " +
            "$COL_AWARDS TEXT, " +
            "$COL_LANGUAGE TEXT, " +
            "$COL_PRODUCTION TEXT, " +
            "$COL_METASCORE TEXT, " +
            "$COL_IMDB_RATING TEXT, " +
            "$COL_IMDB_VOTES TEXT, " +
            "$COL_TOMATO_METER TEXT, " +
            "$COL_TOMATO_IMAGE TEXT, " +
            "$COL_TOMATO_RATING TEXT, " +
            "$COL_TOMATO_URL TEXT, " +
            "$COL_TOMATO_USER_METER TEXT, " +
            "$COL_TOMATO_USER_RATING TEXT);"
}