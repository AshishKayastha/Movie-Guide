package com.ashish.movieguide.data.database.mappers

import com.ashish.movieguide.data.database.entities.PersonEntity
import com.ashish.movieguide.data.network.entities.tmdb.Person
import com.ashish.movieguide.data.network.entities.trakt.TraktPerson
import io.reactivex.functions.BiFunction
import javax.inject.Inject
import javax.inject.Singleton
import com.ashish.movieguide.data.database.entities.Person as LocalPerson

@Singleton
class PersonMapper @Inject constructor() : BiFunction<Person, TraktPerson, LocalPerson> {

    override fun apply(person: Person, traktPerson: TraktPerson?): LocalPerson {
        val personEntity = PersonEntity()
        person.run {
            personEntity.tmdbId = id!!
            personEntity.name = name
            personEntity.profilePath = profilePath
        }

        traktPerson?.addToPersonEntity(personEntity)
        return personEntity
    }
}