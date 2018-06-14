package com.ashish.movieguide.data.local.entities

import android.os.Parcelable
import io.requery.CascadeAction
import io.requery.Column
import io.requery.Entity
import io.requery.Key
import io.requery.ManyToMany
import io.requery.OneToMany
import io.requery.Persistable
import io.requery.Table

@Table(name = "people")
@Entity(name = "PersonEntity")
interface LocalPerson : Persistable, Parcelable {

    @get:Key
    @get:Column(name = "id")
    var tmdbId: Long

    var name: String?

    var birthday: String?

    var biography: String?

    var homepage: String?

    var deathday: String?

    var slug: String?

    @get:Column(name = "trakt_id")
    var traktId: Long?

    @get:Column(name = "imdb_id")
    var imdbId: String?

    @get:Column(name = "tvrage_id")
    var tvrageId: Long?

    @get:Column(name = "profile_path")
    var profilePath: String?

    @get:Column(name = "place_of_birth")
    var placeOfBirth: String?

    @get:Column(name = "also_known_as")
    var alsoKnownAs: String?

    /**********   Relations   **********/

    @get:OneToMany(mappedBy = "id", cascade = [CascadeAction.DELETE])
    val images: MutableSet<LocalImage>?

    @get:ManyToMany
    val credits: MutableSet<LocalCredit>?
}