package com.ashish.movieguide.data.local.entities

import io.requery.Column
import io.requery.Entity
import io.requery.Generated
import io.requery.JunctionTable
import io.requery.Key
import io.requery.ManyToMany
import io.requery.Persistable
import io.requery.Table

@Entity
@Table(name = "similar_contents")
interface SimilarContent : Persistable {

    @get:Key
    @get:Generated
    val id: Long

    @get:Column(name = "media_id")
    var mediaId: Long

    /**********   Relations   **********/

    @get:ManyToMany
    @get:JunctionTable(name = "similar_movies")
    var movie: LocalMovie?

    @get:ManyToMany
    @get:JunctionTable(name = "similar_shows")
    var show: LocalShow?
}