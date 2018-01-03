package com.ashish.movieguide.data.database.entities

import android.os.Parcelable
import io.requery.Column
import io.requery.Entity
import io.requery.ForeignKey
import io.requery.Key
import io.requery.ManyToMany
import io.requery.ManyToOne
import io.requery.OneToMany
import io.requery.OneToOne
import io.requery.Persistable
import io.requery.Table

@Entity
@Table(name = "seasons")
interface Season : Persistable, Parcelable {

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

    @get:Column(name = "air_date")
    var airDate: String?

    @get:Column(name = "poster_path")
    var posterPath: String?

    @get:Column(name = "episode_count")
    var episodeCount: Int?

    @get:Column(name = "season_number")
    var seasonNumber: Int?


    /**********   Relations   **********/

    @get:ManyToOne
    var show: Show?

    @get:ForeignKey
    @get:OneToOne
    var omdb: OMDb?

    @get:OneToMany(mappedBy = "id")
    val episodes: MutableSet<Episode>?

    @get:OneToMany(mappedBy = "id")
    val images: MutableSet<Image>?

    @get:OneToMany(mappedBy = "id")
    val videos: MutableSet<Video>?

    @get:ManyToMany
    val credits: MutableSet<Credit>?
}