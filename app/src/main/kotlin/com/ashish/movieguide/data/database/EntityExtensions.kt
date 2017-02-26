package com.ashish.movieguide.data.database

import com.ashish.movieguide.data.database.entities.CreditEntity
import com.ashish.movieguide.data.database.entities.GenreEntity
import com.ashish.movieguide.data.database.entities.ImageEntity
import com.ashish.movieguide.data.database.entities.MovieDetailEntity
import com.ashish.movieguide.data.database.entities.MovieEntity
import com.ashish.movieguide.data.database.entities.OMDbEntity
import com.ashish.movieguide.data.database.entities.SimilarMovieEntity
import com.ashish.movieguide.data.database.entities.VideoEntity
import com.ashish.movieguide.data.database.tables.CreditsTable
import com.ashish.movieguide.data.database.tables.OMDbTable
import com.ashish.movieguide.data.models.Credit
import com.ashish.movieguide.data.models.CreditResults
import com.ashish.movieguide.data.models.FullDetailContent
import com.ashish.movieguide.data.models.Genre
import com.ashish.movieguide.data.models.ImageItem
import com.ashish.movieguide.data.models.Images
import com.ashish.movieguide.data.models.Movie
import com.ashish.movieguide.data.models.MovieDetail
import com.ashish.movieguide.data.models.OMDbDetail
import com.ashish.movieguide.data.models.VideoItem
import com.ashish.movieguide.data.models.Videos
import com.pushtorefresh.storio.sqlite.StorIOSQLite
import com.pushtorefresh.storio.sqlite.queries.Query
import java.util.ArrayList

/**
 * Created by Ashish on Feb 06.
 */
fun Movie.toEntity(movieType: String) = MovieEntity(id!!, title, overview, voteCount, posterPath, releaseDate,
        voteAverage, backdropPath, movieType = movieType)

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

    val genreList = movieDetail.genres.toGenreEntityList(movieId)

    val creditsResults = movieDetail.creditsResults
    val castList = creditsResults.toCreditEntityList(movieId, CreditsTable.CREDIT_TYE_CAST)
    val crewList = creditsResults.toCreditEntityList(movieId, CreditsTable.CREDIT_TYE_CREW)

    val imageList = movieDetail.images.toImageEntityList(movieId)
    val videoList = movieDetail.videos.toVideoEntityList(movieId)

    val similarMoviesList = movieDetail.similarMovieResults?.results
            ?.map { it.toSimilarEntity(movieId) }
            ?.toList()

    return MovieDetailEntity(movieDetail.toEntity(), omdbDetail?.toEntity(movieId),
            genreList, castList, crewList, imageList, videoList, similarMoviesList)
}

fun List<Genre>?.toGenreEntityList(mediaId: Long) = this?.map { it.toEntity(mediaId) }?.toList()

fun CreditResults?.toCreditEntityList(mediaId: Long, creditType: String)
        = this?.cast?.map { it.toEntity(mediaId, creditType) }?.toList()

fun Images?.toImageEntityList(mediaId: Long): List<ImageEntity>? {
    val imageList = ArrayList<ImageEntity>()
    this?.backdrops?.forEach { imageList.add(it.toEntity(mediaId)) }
    this?.posters?.forEach { imageList.add(it.toEntity(mediaId)) }
    return imageList
}

fun Videos?.toVideoEntityList(mediaId: Long) = this?.results?.map { it.toEntity(mediaId) }?.toList()

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