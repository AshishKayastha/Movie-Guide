package com.ashish.movies.data.database

import com.ashish.movies.data.database.entities.CreditEntity
import com.ashish.movies.data.database.entities.GenreEntity
import com.ashish.movies.data.database.entities.ImageEntity
import com.ashish.movies.data.database.entities.MovieDetailEntity
import com.ashish.movies.data.database.entities.MovieEntity
import com.ashish.movies.data.database.entities.OMDbEntity
import com.ashish.movies.data.database.entities.SimilarMovieEntity
import com.ashish.movies.data.database.entities.VideoEntity
import com.ashish.movies.data.database.tables.CreditsTable
import com.ashish.movies.data.database.tables.OMDbTable
import com.ashish.movies.data.models.Credit
import com.ashish.movies.data.models.FullDetailContent
import com.ashish.movies.data.models.Genre
import com.ashish.movies.data.models.ImageItem
import com.ashish.movies.data.models.Movie
import com.ashish.movies.data.models.MovieDetail
import com.ashish.movies.data.models.OMDbDetail
import com.ashish.movies.data.models.VideoItem
import com.pushtorefresh.storio.sqlite.StorIOSQLite
import com.pushtorefresh.storio.sqlite.queries.Query
import java.util.*

/**
 * Created by Ashish on Feb 06.
 */
fun Movie.toEntity() = MovieEntity(id!!, title, overview, voteCount, posterPath, releaseDate, voteAverage, backdropPath)

fun MovieDetail.toEntity() = MovieEntity(id!!, title, overview, voteCount, posterPath, releaseDate, voteAverage,
        backdropPath, runtime, budget, revenue, status, tagline, imdbId)

fun Genre.toEntity(mediaId: Long) = GenreEntity(id!!, mediaId, name)

fun Credit.toEntity(mediaId: Long, creditType: String) = CreditEntity(id!!, mediaId, creditType,
        name!!, title, job, character, mediaType, posterPath, releaseDate, profilePath)

fun ImageItem.toEntity(mediaId: Long) = ImageEntity(id!!, mediaId, filePath!!)

fun VideoItem.toEntity(mediaId: Long) = VideoEntity(id!!, mediaId, name, site, size, key, type)

fun Movie.toSimilarEntity(mediaId: Long) = SimilarMovieEntity(id!!, mediaId, title, overview, voteCount,
        posterPath, releaseDate, voteAverage, backdropPath)

fun OMDbDetail.toEntity(mediaId: Long) = OMDbEntity(imdbId, mediaId, Rated, Country, Awards, Language, Metascore,
        imdbRating, imdbVotes, tomatoMeter, tomatoImage, tomatoRating, tomatoURL, tomatoUserMeter,
        tomatoUserRating, Production)

fun FullDetailContent<MovieDetail>.toMovieDetailEntity(): MovieDetailEntity {
    val movieDetail = detailContent!!
    val movieId = movieDetail.id!!

    val genreList = movieDetail.genres
            ?.map { it.toEntity(movieId) }
            ?.toList()

    val creditsResults = movieDetail.creditsResults
    val castList = creditsResults?.cast
            ?.map { it.toEntity(movieId, CreditsTable.CREDIT_TYE_CAST) }
            ?.toList()

    val crewList = creditsResults?.crew
            ?.map { it.toEntity(movieId, CreditsTable.CREDIT_TYE_CREW) }
            ?.toList()

    val images = movieDetail.images
    val imageList = ArrayList<ImageEntity>()
    images?.backdrops?.forEach { imageList.add(it.toEntity(movieId)) }
    images?.posters?.forEach { imageList.add(it.toEntity(movieId)) }

    val videoList = movieDetail.videos?.results
            ?.map { it.toEntity(movieId) }
            ?.toList()

    val similarMoviesList = movieDetail.similarMovieResults?.results
            ?.map { it.toSimilarEntity(movieId) }
            ?.toList()

    return MovieDetailEntity(movieDetail.toEntity(), omdbDetail?.toEntity(movieId),
            genreList, castList, crewList, imageList, videoList, similarMoviesList)
}

inline fun <reified T : Any> StorIOSQLite.getContentList(mediaId: Long, tableName: String,
                                                         colName: String = "media_id"): List<T>? {
    return this.get()
            .listOfObjects(T::class.java)
            .withQuery(Query.builder()
                    .table(tableName)
                    .where("$colName = ?")
                    .whereArgs(mediaId)
                    .build())
            .prepare()
            .executeAsBlocking()
}

fun StorIOSQLite.getOMDbEntity(mediaId: Long): OMDbEntity? {
    return this.get()
            .`object`(OMDbEntity::class.java)
            .withQuery(Query.builder()
                    .table(OMDbTable.TABLE_NAME)
                    .where("${OMDbTable.COL_MEDIA_ID} = ?")
                    .whereArgs(mediaId)
                    .build())
            .prepare()
            .executeAsBlocking()
}