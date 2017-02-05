package com.ashish.movies.data.database.resolvers.moviedetail

import android.database.Cursor
import com.ashish.movies.data.database.entities.GenreEntity
import com.ashish.movies.data.database.entities.ImageEntity
import com.ashish.movies.data.database.entities.MovieDetailEntity
import com.ashish.movies.data.database.entities.VideoEntity
import com.ashish.movies.data.database.resolvers.movie.MovieGetResolver
import com.ashish.movies.data.database.tables.GenresTable
import com.ashish.movies.data.database.tables.ImagesTable
import com.ashish.movies.data.database.tables.VideosTable
import com.pushtorefresh.storio.sqlite.StorIOSQLite
import com.pushtorefresh.storio.sqlite.operations.get.GetResolver
import com.pushtorefresh.storio.sqlite.queries.Query
import com.pushtorefresh.storio.sqlite.queries.RawQuery

/**
 * Created by Ashish on Feb 05.
 */
class MovieDetailGetResolver(private val movieGetResolver: MovieGetResolver) : GetResolver<MovieDetailEntity>() {

    private val storIOSQLiteFromPerformGet = ThreadLocal<StorIOSQLite>()

    override fun mapFromCursor(cursor: Cursor): MovieDetailEntity {
        val storIOSQLite = storIOSQLiteFromPerformGet.get()
        val movieEntity = movieGetResolver.mapFromCursor(cursor)

        val mediaId = movieEntity.id
        val genreList = getContentList<GenreEntity>(mediaId, GenresTable.TABLE_NAME, storIOSQLite)
        val imageList = getContentList<ImageEntity>(mediaId, ImagesTable.TABLE_NAME, storIOSQLite)
        val videoList = getContentList<VideoEntity>(mediaId, VideosTable.TABLE_NAME, storIOSQLite)

        return MovieDetailEntity(movieEntity, genreList, images = imageList, videos = videoList)
    }

    private inline fun <reified T : Any> getContentList(mediaId: Long, tableName: String,
                                                        storIOSQLite: StorIOSQLite): List<T>? {
        return storIOSQLite
                .get()
                .listOfObjects(T::class.java)
                .withQuery(Query.builder()
                        .table(tableName)
                        .where("media_id = ?")
                        .whereArgs(mediaId)
                        .build())
                .prepare()
                .executeAsBlocking()
    }

    override fun performGet(storIOSQLite: StorIOSQLite, rawQuery: RawQuery): Cursor {
        storIOSQLiteFromPerformGet.set(storIOSQLite)
        return storIOSQLite.lowLevel().rawQuery(rawQuery)
    }

    override fun performGet(storIOSQLite: StorIOSQLite, query: Query): Cursor {
        storIOSQLiteFromPerformGet.set(storIOSQLite)
        return storIOSQLite.lowLevel().query(query)
    }
}