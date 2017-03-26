package com.ashish.movieguide.data.database.entities

import android.os.Parcelable
import io.requery.Entity
import io.requery.JunctionTable
import io.requery.Key
import io.requery.ManyToMany
import io.requery.Persistable
import io.requery.Table

@Entity
@Table(name = "genres")
interface Genre : Persistable, Parcelable {

    @get:Key
    var id: Long

    var name: String?


    /**********   Relations   **********/

    @get:ManyToMany
    @get:JunctionTable(name = "movie_genres")
    var movie: Movie?

    @get:ManyToMany
    @get:JunctionTable(name = "show_genres")
    var show: Show?
}