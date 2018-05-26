package com.ashish.movieguide.data.database.mappers

import com.ashish.movieguide.data.database.entities.PersonEntity
import com.ashish.movieguide.data.network.entities.tmdb.Person
import com.ashish.movieguide.data.network.entities.trakt.TraktPerson
import com.ashish.movieguide.utils.BiFunction
import javax.inject.Inject
import javax.inject.Singleton
import com.ashish.movieguide.data.database.entities.Person as LocalPerson

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