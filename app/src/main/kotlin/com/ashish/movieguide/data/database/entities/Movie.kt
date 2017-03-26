package com.ashish.movieguide.data.database.entities

import android.os.Parcelable
import io.requery.Column
import io.requery.Entity
import io.requery.ForeignKey
import io.requery.Index
import io.requery.Key
import io.requery.ManyToMany
import io.requery.OneToMany
import io.requery.OneToOne
import io.requery.Persistable
import io.requery.Table

@Entity
@Table(name = "movies")
interface Movie : Persistable, Parcelable {

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
    @get:Index(value = "release_date_index")
    var releaseDate: String?

    @get:Column(name = "poster_path")
    var posterPath: String?

    @get:Column(name = "backdrop_path")
    var backdropPath: String?

    @get:Column(name = "user_rating")
    var userRating: Int?

    @get:Column(name = "in_watchlist")
    var inWatchlist: Boolean


    /**********   Relations   **********/

    @get:ForeignKey
    @get:OneToOne
    var omdb: OMDb?

    @get:OneToMany
    val images: MutableSet<Image>?

    @get:OneToMany
    val videos: MutableSet<Video>?

    @get:ManyToMany
    val credits: MutableSet<Credit>?

    @get:ManyToMany
    val genres: MutableSet<Genre>?

    @get:ManyToMany
    val similarMovies: MutableSet<SimilarContent>?
}