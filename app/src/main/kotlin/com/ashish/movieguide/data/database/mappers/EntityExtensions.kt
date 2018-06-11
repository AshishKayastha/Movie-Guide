package com.ashish.movieguide.data.database.mappers

import com.ashish.movieguide.data.database.entities.CreditEntity
import com.ashish.movieguide.data.database.entities.Episode
import com.ashish.movieguide.data.database.entities.GenreEntity
import com.ashish.movieguide.data.database.entities.Image
import com.ashish.movieguide.data.database.entities.ImageEntity
import com.ashish.movieguide.data.database.entities.Movie
import com.ashish.movieguide.data.database.entities.OMDb
import com.ashish.movieguide.data.database.entities.OMDbEntity
import com.ashish.movieguide.data.database.entities.Season
import com.ashish.movieguide.data.database.entities.Show
import com.ashish.movieguide.data.database.entities.Video
import com.ashish.movieguide.data.database.entities.VideoEntity
import com.ashish.movieguide.data.network.entities.common.OMDbDetail
import com.ashish.movieguide.data.network.entities.tmdb.Credit
import com.ashish.movieguide.data.network.entities.tmdb.Genre
import com.ashish.movieguide.data.network.entities.tmdb.ImageItem
import com.ashish.movieguide.data.network.entities.tmdb.Person
import com.ashish.movieguide.data.network.entities.tmdb.VideoItem
import com.ashish.movieguide.data.network.entities.trakt.TraktEpisode
import com.ashish.movieguide.data.network.entities.trakt.TraktMovie
import com.ashish.movieguide.data.network.entities.trakt.TraktPerson
import com.ashish.movieguide.data.network.entities.trakt.TraktSeason
import com.ashish.movieguide.data.network.entities.trakt.TraktShow
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty

fun Credit.toEntity(isCast: Boolean): com.ashish.movieguide.data.database.entities.Credit {
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

fun Genre.toEntity(): com.ashish.movieguide.data.database.entities.Genre {
    val genreEntity = GenreEntity()
    genreEntity.id = id!!
    genreEntity.name = name
    return genreEntity
}

fun ImageItem.toEntity(): Image {
    val imageEntity = ImageEntity()
    imageEntity.filePath = filePath
    return imageEntity
}

fun VideoItem.toEntity(): Video {
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

fun OMDbDetail.toEntity(): OMDb? {
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

fun TraktMovie.addToMovieEntity(movieEntity: Movie) {
    with(this) {
        movieEntity.releaseYear = year
        movieEntity.slug = ids?.slug
        movieEntity.imdbId = ids?.imdb
        movieEntity.traktId = ids?.trakt
    }
}

fun TraktShow.addToShowEntity(showEntity: Show) {
    with(this) {
        showEntity.releaseYear = year
        showEntity.slug = ids?.slug
        showEntity.imdbId = ids?.imdb
        showEntity.traktId = ids?.trakt
        showEntity.tvdbId = ids?.tvdb
        showEntity.tvrageId = ids?.tvrage
    }
}

fun TraktSeason.addToSeasonEntity(seasonEntity: Season) {
    with(this) {
        seasonEntity.traktId = ids?.trakt
        seasonEntity.tvdbId = ids?.tvdb
        seasonEntity.tvrageId = ids?.tvrage
    }
}

fun TraktEpisode.addToEpisodeEntity(episodeEntity: Episode) {
    with(this) {
        episodeEntity.imdbId = ids?.imdb
        episodeEntity.traktId = ids?.trakt
        episodeEntity.tvdbId = ids?.tvdb
        episodeEntity.tvrageId = ids?.tvrage
    }
}

fun TraktPerson.addToPersonEntity(personEntity: com.ashish.movieguide.data.database.entities.Person) {
    with(this) {
        personEntity.slug = ids?.slug
        personEntity.imdbId = ids?.imdb
        personEntity.traktId = ids?.trakt
        personEntity.tvrageId = ids?.tvrage
    }
}