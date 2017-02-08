package com.ashish.movies.data.database.tables

/**
 * Created by Ashish on Feb 03.
 */
object MoviesTable : DatabaseTable {

    override val TABLE_NAME = "movies"

    const val COL_ID = "_id"
    const val COL_TITLE = "title"
    const val COL_OVERVIEW = "overview"
    const val COL_BUDGET = "budget"
    const val COL_RUNTIME = "runtime"
    const val COL_REVENUE = "revenue"
    const val COL_STATUS = "status"
    const val COL_TAGLINE = "tagline"
    const val COL_IMDB_ID = "imdb_id"
    const val COL_VOTE_COUNT = "vote_count"
    const val COL_POSTER_PATH = "poster_path"
    const val COL_RELEASE_DATE = "release_date"
    const val COL_VOTE_AVERAGE = "vote_average"
    const val COL_BACKDROP_PATH = "backdrop_path"

    override val CREATE_TABLE = "CREATE TABLE $TABLE_NAME(" +
            "$COL_ID INTEGER NOT NULL PRIMARY KEY, " +
            "$COL_TITLE TEXT NOT NULL, " +
            "$COL_OVERVIEW TEXT, " +
            "$COL_BUDGET INTEGER, " +
            "$COL_RUNTIME INTEGER, " +
            "$COL_REVENUE INTEGER, " +
            "$COL_STATUS TEXT, " +
            "$COL_TAGLINE TEXT, " +
            "$COL_IMDB_ID TEXT, " +
            "$COL_RELEASE_DATE TEXT, " +
            "$COL_VOTE_COUNT INTEGER, " +
            "$COL_VOTE_AVERAGE INTEGER, " +
            "$COL_POSTER_PATH TEXT, " +
            "$COL_BACKDROP_PATH TEXT);"
}