package com.ashish.movieguide.data.local.entities

import android.os.Parcelable
import io.requery.CascadeAction
import io.requery.Column
import io.requery.Entity
import io.requery.ForeignKey
import io.requery.Key
import io.requery.ManyToMany
import io.requery.OneToMany
import io.requery.OneToOne
import io.requery.Persistable
import io.requery.Table

@Table(name = "movies")
@Entity(name = "MovieEntity")
interface LocalMovie : Persistable, Parcelable {

    @get:Key
    @get:Column(name = "id")
    var tmdbId: Long

    var overview: String?

    var title: String?

    var budget: Int?

    var runtime: Int?

    var revenue: Int?

    var status: String?

    var tagline: String?

    var slug: String?

    @get:Column(name = "imdb_id")
    var imdbId: String?

    @get:Column(name = "trakt_id")
    var traktId: Long?

    @get:Column(name = "release_year")
    var releaseYear: Int?

    @get:Column(name = "vote_count")
    var voteCount: Int?

    @get:Column(name = "vote_average")
    var voteAverage: Double?

    @get:Column(name = "release_date")
    var releaseDate: String?

    @get:Column(name = "poster_path")
    var posterPath: String?

    @get:Column(name = "backdrop_path")
    var backdropPath: String?

    @get:Column(name = "user_rating")
    var userRating: Int?

    @get:Column(name = "in_watchlist")
    var inWatchlist: Boolean

    @get:Column(name = "is_favorite")
    var isFavorite: Boolean

    /**********   Relations   **********/

    @get:ForeignKey
    @get:OneToOne
    var omdb: LocalOMDb?

    @get:OneToMany(mappedBy = "id", cascade = [CascadeAction.DELETE])
    val images: MutableSet<LocalImage>?

    @get:OneToMany(mappedBy = "id", cascade = [CascadeAction.DELETE])
    val videos: MutableSet<LocalVideo>?

    @get:ManyToMany
    val credits: MutableSet<LocalCredit>?

    @get:ManyToMany
    val genres: MutableSet<LocalGenre>?

    @get:ManyToMany
    val similarMovies: MutableSet<SimilarContent>?
}