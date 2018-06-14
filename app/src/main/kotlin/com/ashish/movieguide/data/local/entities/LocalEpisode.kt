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

@Table(name = "episodes")
@Entity(name = "EpisodeEntity")
interface LocalEpisode : Persistable, Parcelable {

    @get:Key
    @get:Column(name = "id")
    var tmdbId: Long

    var name: String?

    var overview: String?

    @get:Column(name = "imdb_id")
    var imdbId: String?

    @get:Column(name = "trakt_id")
    var traktId: Long?

    @get:Column(name = "tvdb_id")
    var tvdbId: Long?

    @get:Column(name = "tvrage_id")
    var tvrageId: Long?

    @get:Column(name = "vote_count")
    var voteCount: Int?

    @get:Column(name = "vote_average")
    var voteAverage: Double?

    @get:Column(name = "air_date")
    var airDate: String?

    @get:Column(name = "still_path")
    var stillPath: String?

    @get:Column(name = "season_number")
    var seasonNumber: Int?

    @get:Column(name = "episode_number")
    var episodeNumber: Int?

    @get:Column(name = "user_rating")
    var userRating: Int?

    @get:Column(name = "in_watchlist")
    var inWatchlist: Boolean

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
}