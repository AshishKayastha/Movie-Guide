package com.ashish.movieguide.data.database.entities

import io.requery.Entity
import io.requery.Key
import io.requery.ManyToOne
import io.requery.Persistable
import io.requery.Table

@Entity
@Table(name = "videos")
interface Video : Persistable {

    @get:Key
    var id: String

    var name: String?

    var site: String?

    var key: String?

    var type: String?


    /**********   Relations   **********/

    @get:ManyToOne
    var movie: Movie?

    @get:ManyToOne
    var show: Show?

    @get:ManyToOne
    var season: Season?

    @get:ManyToOne
    var episode: Episode?
}