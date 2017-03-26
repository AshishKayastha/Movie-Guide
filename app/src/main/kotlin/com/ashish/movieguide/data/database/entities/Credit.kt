package com.ashish.movieguide.data.database.entities

import io.requery.Column
import io.requery.Entity
import io.requery.Generated
import io.requery.JunctionTable
import io.requery.Key
import io.requery.ManyToMany
import io.requery.Persistable
import io.requery.Table

@Entity
@Table(name = "credits")
interface Credit : Persistable {

    @get:Key
    @get:Generated
    val id: Long

    @get:Column(name = "tmdb_id")
    var tmdbId: Long

    var job: String?

    @get:Column(name = "character_name")
    var characterName: String?

    //    var name: String?

    //    var title: String?

    @get:Column(name = "is_cast")
    var isCast: Boolean

    @get:Column(name = "media_type")
    var mediaType: String?

    //    @get:Column(name = "poster_path")
    //    var posterPath: String?
    //
    //    @get:Column(name = "release_date")
    //    var releaseDate: String?
    //
    //    @get:Column(name = "profile_path")
    //    var profilePath: String?


    /**********   Relations   **********/

    @get:ManyToMany
    @get:JunctionTable(name = "movie_credit")
    var movie: Movie?

    @get:ManyToMany
    @get:JunctionTable(name = "show_credit")
    var show: Show?

    @get:ManyToMany
    @get:JunctionTable(name = "season_credit")
    var season: Season?

    @get:ManyToMany
    @get:JunctionTable(name = "episode_credit")
    var episode: Episode?

    @get:ManyToMany
    @get:JunctionTable(name = "person_credit")
    var person: Person?
}