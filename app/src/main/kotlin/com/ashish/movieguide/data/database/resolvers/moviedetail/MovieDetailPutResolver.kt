package com.ashish.movieguide.data.database.resolvers.moviedetail

import com.ashish.movieguide.data.database.entities.MovieDetailEntity
import com.ashish.movieguide.data.database.tables.CreditsTable
import com.ashish.movieguide.data.database.tables.GenresTable
import com.ashish.movieguide.data.database.tables.ImagesTable
import com.ashish.movieguide.data.database.tables.MoviesTable
import com.ashish.movieguide.data.database.tables.OMDbTable
import com.ashish.movieguide.data.database.tables.SimilarMoviesTable
import com.ashish.movieguide.data.database.tables.VideosTable
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.pushtorefresh.storio.sqlite.StorIOSQLite
import com.pushtorefresh.storio.sqlite.operations.put.PutResolver
import com.pushtorefresh.storio.sqlite.operations.put.PutResult
import java.util.ArrayList
import java.util.HashSet

/**
 * Created by Ashish on Feb 05.
 */
class MovieDetailPutResolver : PutResolver<MovieDetailEntity>() {

    override fun performPut(storIOSQLite: StorIOSQLite, movieDetailEntity: MovieDetailEntity): PutResult {
        val omdbContent = movieDetailEntity.omdbEntity
        val omdbSize = if (omdbContent != null) 1 else 0

        val genres = movieDetailEntity.genres
        val genreListSize = genres?.size ?: 0

        val images = movieDetailEntity.images
        val imageListSize = images?.size ?: 0

        val videos = movieDetailEntity.videos
        val videoListSize = videos?.size ?: 0

        val cast = movieDetailEntity.cast
        val castListSize = cast?.size ?: 0

        val crew = movieDetailEntity.crew
        val crewListSize = crew?.size ?: 0

        val similarMovies = movieDetailEntity.similarMovies
        val similarMoviesListSize = similarMovies?.size ?: 0

        val objectsToPut = ArrayList<Any>(1 + omdbSize + genreListSize + imageListSize + videoListSize
                + castListSize + crewListSize + similarMoviesListSize)

        var affectedTableSize = 1
        objectsToPut.add(movieDetailEntity.movieEntity)

        if (omdbContent != null) {
            objectsToPut.add(omdbContent)
            affectedTableSize++
        }

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

        if (cast.isNotNullOrEmpty()) {
            objectsToPut.addAll(cast!!)
            affectedTableSize++
        }

        if (crew.isNotNullOrEmpty()) {
            objectsToPut.addAll(crew!!)
            affectedTableSize++
        }

        if (similarMovies.isNotNullOrEmpty()) {
            objectsToPut.addAll(similarMovies!!)
            affectedTableSize++
        }

        storIOSQLite
                .put()
                .objects(objectsToPut)
                .prepare()
                .executeAsBlocking()

        val affectedTables = HashSet<String>(affectedTableSize)
        affectedTables.add(MoviesTable.TABLE_NAME)

        if (omdbContent != null) affectedTables.add(OMDbTable.TABLE_NAME)
        if (genreListSize > 0) affectedTables.add(GenresTable.TABLE_NAME)
        if (imageListSize > 0) affectedTables.add(ImagesTable.TABLE_NAME)
        if (videoListSize > 0) affectedTables.add(VideosTable.TABLE_NAME)
        if (castListSize > 0) affectedTables.add(CreditsTable.TABLE_NAME)
        if (crewListSize > 0) affectedTables.add(CreditsTable.TABLE_NAME)
        if (similarMoviesListSize > 0) affectedTables.add(SimilarMoviesTable.TABLE_NAME)

        return PutResult.newUpdateResult(objectsToPut.size, affectedTables)
    }
}