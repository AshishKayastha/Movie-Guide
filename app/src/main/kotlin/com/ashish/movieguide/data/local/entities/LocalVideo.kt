package com.ashish.movieguide.data.local.entities

import io.requery.Entity
import io.requery.Key
import io.requery.Persistable
import io.requery.Table

@Table(name = "videos")
@Entity(name = "VideoEntity")
interface LocalVideo : Persistable {

    @get:Key
    var id: String

    var name: String?

    var site: String?

    var key: String?

    var type: String?
}