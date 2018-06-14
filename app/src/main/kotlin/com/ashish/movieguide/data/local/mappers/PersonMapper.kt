package com.ashish.movieguide.data.local.mappers

import com.ashish.movieguide.data.local.entities.LocalPerson
import com.ashish.movieguide.data.local.entities.PersonEntity
import com.ashish.movieguide.data.remote.entities.tmdb.Person
import com.ashish.movieguide.data.remote.entities.trakt.TraktPerson
import com.ashish.movieguide.utils.BiFunction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonMapper @Inject constructor() : BiFunction<Person, TraktPerson, LocalPerson> {

    override fun apply(var1: Person, var2: TraktPerson?): LocalPerson {
        val personEntity = PersonEntity()
        var1.apply {
            personEntity.tmdbId = id!!
            personEntity.name = name
            personEntity.profilePath = profilePath
        }

        var2?.addToPersonEntity(personEntity)
        return personEntity
    }
}