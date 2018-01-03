package com.ashish.movieguide.data.database.entities

import io.requery.Column
import io.requery.Entity
import io.requery.Generated
import io.requery.Key
import io.requery.Persistable
import io.requery.Table

@Entity
@Table(name = "images")
interface Image : Persistable {

    @get:Key
    @get:Generated
    val id: Long

    @get:Column(name = "file_path", unique = true)
    var filePath: String?
}