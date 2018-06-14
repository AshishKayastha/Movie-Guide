package com.ashish.movieguide.data.local.entities

import io.requery.Column
import io.requery.Entity
import io.requery.Generated
import io.requery.Key
import io.requery.Persistable
import io.requery.Table

@Table(name = "images")
@Entity(name = "ImageEntity")
interface LocalImage : Persistable {

    @get:Key
    @get:Generated
    val id: Long

    @get:Column(name = "file_path", unique = true)
    var filePath: String?
}