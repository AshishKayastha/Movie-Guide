package com.ashish.movieguide.data.database.resolvers.movie

import android.database.Cursor
import com.ashish.movieguide.data.database.entities.MovieEntity
import com.ashish.movieguide.data.database.tables.MoviesTable
import com.ashish.movieguide.utils.extensions.getDoubleByColumnName
import com.ashish.movieguide.utils.extensions.getIntByColumnName
import com.ashish.movieguide.utils.extensions.getLongByColumnName
import com.ashish.movieguide.utils.extensions.getStringByColumnName
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver

/**
 * Created by Ashish on Feb 05.
 */
class MovieGetResolver : DefaultGetResolver<MovieEntity>() {

    override fun mapFromCursor(cursor: Cursor) = MovieEntity(
            id = cursor.getLongByColumnName(MoviesTable.COL_ID),
            title = cursor.getStringByColumnName(MoviesTable.COL_TITLE),
            overview = cursor.getStringByColumnName(MoviesTable.COL_OVERVIEW),
            voteCount = cursor.getIntByColumnName(MoviesTable.COL_VOTE_COUNT),
            posterPath = cursor.getStringByColumnName(MoviesTable.COL_POSTER_PATH),
            releaseDate = cursor.getStringByColumnName(MoviesTable.COL_RELEASE_DATE),
            voteAverage = cursor.getDoubleByColumnName(MoviesTable.COL_VOTE_AVERAGE),
            backdropPath = cursor.getStringByColumnName(MoviesTable.COL_BACKDROP_PATH),
            runtime = cursor.getIntByColumnName(MoviesTable.COL_RUNTIME),
            budget = cursor.getIntByColumnName(MoviesTable.COL_BUDGET),
            revenue = cursor.getIntByColumnName(MoviesTable.COL_REVENUE),
            status = cursor.getStringByColumnName(MoviesTable.COL_STATUS),
            tagline = cursor.getStringByColumnName(MoviesTable.COL_TAGLINE),
            imdbId = cursor.getStringByColumnName(MoviesTable.COL_IMDB_ID)
    )
}