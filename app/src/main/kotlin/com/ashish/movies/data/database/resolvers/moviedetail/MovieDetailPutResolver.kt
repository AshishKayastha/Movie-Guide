package com.ashish.movies.data.database.resolvers.moviedetail

import com.ashish.movies.data.database.entities.MovieDetailEntity
import com.ashish.movies.data.database.tables.GenresTable
import com.ashish.movies.data.database.tables.ImagesTable
import com.ashish.movies.data.database.tables.MoviesTable
import com.ashish.movies.data.database.tables.VideosTable
import com.ashish.movies.utils.extensions.isNotNullOrEmpty
import com.pushtorefresh.storio.sqlite.StorIOSQLite
import com.pushtorefresh.storio.sqlite.operations.put.PutResolver
import com.pushtorefresh.storio.sqlite.operations.put.PutResult
import java.util.*

/**
 * Created by Ashish on Feb 05.
 */
class MovieDetailPutResolver : PutResolver<MovieDetailEntity>() {

    override fun performPut(storIOSQLite: StorIOSQLite, movieDetailEntity: MovieDetailEntity): PutResult {
        val genres = movieDetailEntity.genres
        val genreListSize = genres?.size ?: 0

        val images = movieDetailEntity.images
        val imageListSize = images?.size ?: 0

        val videos = movieDetailEntity.videos
        val videoListSize = videos?.size ?: 0

        val objectsToPut = ArrayList<Any>(1 + genreListSize + imageListSize + videoListSize)

        var affectedTableSize = 1
        objectsToPut.add(movieDetailEntity.movieEntity)

        if (genres.isNotNullOrEmpty()) {
            objectsToPut.addAll(genres!!)
            affectedTableSize++
        }

        if (images.isNotNullOrEmpty()) {
            objectsToPut.addAll(images!!)
            affectedTableSize++
        }

        if (videos.isNotNullOrEmpty()) {
            objectsToPut.addAll(videos!!)
            affectedTableSize++
        }

        storIOSQLite
                .put()
                .objects(objectsToPut)
                .prepare()
                .executeAsBlocking()

        val affectedTables = HashSet<String>(affectedTableSize)
        affectedTables.add(MoviesTable.TABLE_NAME)
        if (genreListSize > 0) affectedTables.add(GenresTable.TABLE_NAME)
        if (imageListSize > 0) affectedTables.add(ImagesTable.TABLE_NAME)
        if (videoListSize > 0) affectedTables.add(VideosTable.TABLE_NAME)

        return PutResult.newUpdateResult(objectsToPut.size, affectedTables)
    }
}