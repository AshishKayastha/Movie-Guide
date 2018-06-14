package com.ashish.movieguide.data.local.entities

import io.requery.Column
import io.requery.Entity
import io.requery.Key
import io.requery.OneToOne
import io.requery.Persistable
import io.requery.Table

@Table(name = "omdb")
@Entity(name = "OMDbEntity")
interface LocalOMDb : Persistable {

    @get:Key
    @get:Column(name = "id")
    var imdbId: String

    var country: String?

    var awards: String?

    var metascore: String?

    var production: String?

    var certification: String?

    @get:Column(name = "media_language")
    var mediaLanguage: String?

    @get:Column(name = "imdb_rating")
    var imdbRating: String?

    @get:Column(name = "imdb_votes")
    var imdbVotes: String?

    @get:Column(name = "tomato_meter")
    var tomatoMeter: String?

    @get:Column(name = "tomato_image")
    var tomatoImage: String?

    @get:Column(name = "tomato_rating")
    var tomatoRating: String?

    @get:Column(name = "tomato_url")
    var tomatoURL: String?

    @get:Column(name = "tomato_user_meter")
    var tomatoUserMeter: String?

    @get:Column(name = "tomato_user_rating")
    var tomatoUserRating: String?

    /**********   Relations   **********/

    @get:OneToOne
    var movie: LocalMovie?

    @get:OneToOne
    var show: LocalShow?

    @get:OneToOne
    var season: LocalSeason?

    @get:OneToOne
    var episode: LocalEpisode?
}