package com.ashish.movieguide.data.local.entities

import android.os.Parcelable
import io.requery.Entity
import io.requery.JunctionTable
import io.requery.Key
import io.requery.ManyToMany
import io.requery.Persistable
import io.requery.Table

@Table(name = "genres")
@Entity(name = "GenreEntity")
interface LocalGenre : Persistable, Parcelable {

    @get:Key
    var id: Long

    var name: String?

    /**********   Relations   **********/

    @get:ManyToMany
    @get:JunctionTable(name = "movie_genres")
    var movie: LocalMovie?

    @get:ManyToMany
    @get:JunctionTable(name = "show_genres")
    var show: LocalShow?
}