package com.ashish.movieguide.data.database.entities

import android.os.Parcelable
import io.requery.Column
import io.requery.Entity
import io.requery.ForeignKey
import io.requery.Key
import io.requery.ManyToMany
import io.requery.OneToMany
import io.requery.OneToOne
import io.requery.Persistable
import io.requery.Table

@Entity
@Table(name = "shows")
interface Show : Persistable, Parcelable {

    @get:Key
    @get:Column(name = "id")
    var tmdbId: Long

    var name: String?

    var overview: String?

    var type: String?

    var status: String?

    var homepage: String?

    var networks: String?

    var slug: String?

    @get:Column(name = "imdb_id")
    var imdbId: String?

    @get:Column(name = "trakt_id")
    var traktId: Long?

    @get:Column(name = "tvdb_id")
    var tvdbId: Long?

    @get:Column(name = "tvrage_id")
    var tvrageId: Long?

    @get:Column(name = "release_year")
    var releaseYear: Int?

    @get:Column(name = "vote_count")
    var voteCount: Int?

    @get:Column(name = "vote_average")
    var voteAverage: Double?

    @get:Column(name = "poster_path")
    var posterPath: String?

    @get:Column(name = "backdrop_path")
    var backdropPath: String?

    @get:Column(name = "first_air_date")
    var firstAirDate: String?

    @get:Column(name = "last_air_date")
    var lastAirDate: String?

    @get:Column(name = "num_of_seasons")
    var numberOfSeasons: Int?

    @get:Column(name = "num_of_episodes")
    var numberOfEpisodes: Int?

    // TODO: Change this to Int only
    //    @get:Column(name = "episode_run_time")
    //    var episodeRunTime: MutableList<Int>?

    @get:Column(name = "user_rating")
    var userRating: Int?

    @get:Column(name = "in_watchlist")
    var inWatchlist: Boolean


    /**********   Relations   **********/

    @get:ForeignKey
    @get:OneToOne
    var omdb: OMDb?

    @get:OneToMany(mappedBy = "id")
    val seasons: MutableSet<Season>?

    @get:OneToMany(mappedBy = "id")
    val images: MutableSet<Image>?

    @get:OneToMany(mappedBy = "id")
    val videos: MutableSet<Video>?

    @get:ManyToMany
    val credits: MutableSet<Credit>?

    @get:ManyToMany
    val genres: MutableSet<Genre>?

    @get:ManyToMany
    val similarShows: MutableSet<SimilarContent>?
}