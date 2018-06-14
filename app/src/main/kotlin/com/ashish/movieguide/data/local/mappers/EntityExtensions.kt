package com.ashish.movieguide.data.local.mappers

import com.ashish.movieguide.data.local.entities.CreditEntity
import com.ashish.movieguide.data.local.entities.GenreEntity
import com.ashish.movieguide.data.local.entities.ImageEntity
import com.ashish.movieguide.data.local.entities.LocalCredit
import com.ashish.movieguide.data.local.entities.LocalEpisode
import com.ashish.movieguide.data.local.entities.LocalGenre
import com.ashish.movieguide.data.local.entities.LocalImage
import com.ashish.movieguide.data.local.entities.LocalMovie
import com.ashish.movieguide.data.local.entities.LocalOMDb
import com.ashish.movieguide.data.local.entities.LocalPerson
import com.ashish.movieguide.data.local.entities.LocalSeason
import com.ashish.movieguide.data.local.entities.LocalShow
import com.ashish.movieguide.data.local.entities.LocalVideo
import com.ashish.movieguide.data.local.entities.OMDbEntity
import com.ashish.movieguide.data.local.entities.VideoEntity
import com.ashish.movieguide.data.remote.entities.common.OMDbDetail
import com.ashish.movieguide.data.remote.entities.tmdb.Credit
import com.ashish.movieguide.data.remote.entities.tmdb.Genre
import com.ashish.movieguide.data.remote.entities.tmdb.ImageItem
import com.ashish.movieguide.data.remote.entities.tmdb.Person
import com.ashish.movieguide.data.remote.entities.tmdb.VideoItem
import com.ashish.movieguide.data.remote.entities.trakt.TraktEpisode
import com.ashish.movieguide.data.remote.entities.trakt.TraktMovie
import com.ashish.movieguide.data.remote.entities.trakt.TraktPerson
import com.ashish.movieguide.data.remote.entities.trakt.TraktSeason
import com.ashish.movieguide.data.remote.entities.trakt.TraktShow
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty

fun Credit.toEntity(isCast: Boolean): LocalCredit {
    val creditEntity = CreditEntity()
    with(this) {
        creditEntity.tmdbId = id!!
        creditEntity.job = job
        creditEntity.isCast = isCast
        //        creditEntity.name = name
        //        creditEntity.title = title
        //        creditEntity.posterPath = posterPath
        //        creditEntity.profilePath = profilePath
        //        creditEntity.releaseDate = releaseDate
        creditEntity.mediaType = mediaType
        creditEntity.characterName = character
    }
    return creditEntity
}

fun Genre.toEntity(): LocalGenre {
    val genreEntity = GenreEntity()
    genreEntity.id = id!!
    genreEntity.name = name
    return genreEntity
}

fun ImageItem.toEntity(): LocalImage {
    val imageEntity = ImageEntity()
    imageEntity.filePath = filePath
    return imageEntity
}

fun VideoItem.toEntity(): LocalVideo {
    val videoEntity = VideoEntity()
    with(this) {
        videoEntity.id = id!!
        videoEntity.key = key
        videoEntity.name = name
        videoEntity.site = site
        videoEntity.type = type
    }
    return videoEntity
}

fun OMDbDetail.toEntity(): LocalOMDb? {
    if (imdbID.isNotNullOrEmpty()) {
        val omdbEntity = OMDbEntity()
        with(this) {
            omdbEntity.imdbId = imdbID!!
            omdbEntity.awards = Awards
            omdbEntity.country = Country
            omdbEntity.certification = Rated
            omdbEntity.mediaLanguage = Language
            omdbEntity.imdbRating = imdbRating
            omdbEntity.imdbVotes = imdbVotes
            omdbEntity.tomatoURL = tomatoURL
            omdbEntity.metascore = Metascore
            omdbEntity.production = Production
        }
        return omdbEntity
    }
    return null
}

fun Credit.toPerson(): Person = Person(id, name, profilePath)

fun TraktMovie.addToMovieEntity(movieEntity: LocalMovie) {
    with(this) {
        movieEntity.releaseYear = year
        movieEntity.slug = ids?.slug
        movieEntity.imdbId = ids?.imdb
        movieEntity.traktId = ids?.trakt
    }
}

fun TraktShow.addToShowEntity(showEntity: LocalShow) {
    with(this) {
        showEntity.releaseYear = year
        showEntity.slug = ids?.slug
        showEntity.imdbId = ids?.imdb
        showEntity.traktId = ids?.trakt
        showEntity.tvdbId = ids?.tvdb
        showEntity.tvrageId = ids?.tvrage
    }
}

fun TraktSeason.addToSeasonEntity(seasonEntity: LocalSeason) {
    with(this) {
        seasonEntity.traktId = ids?.trakt
        seasonEntity.tvdbId = ids?.tvdb
        seasonEntity.tvrageId = ids?.tvrage
    }
}

fun TraktEpisode.addToEpisodeEntity(episodeEntity: LocalEpisode) {
    with(this) {
        episodeEntity.imdbId = ids?.imdb
        episodeEntity.traktId = ids?.trakt
        episodeEntity.tvdbId = ids?.tvdb
        episodeEntity.tvrageId = ids?.tvrage
    }
}

fun TraktPerson.addToPersonEntity(personEntity: LocalPerson) {
    with(this) {
        personEntity.slug = ids?.slug
        personEntity.imdbId = ids?.imdb
        personEntity.traktId = ids?.trakt
        personEntity.tvrageId = ids?.tvrage
    }
}