package com.ashish.movieguide.data.database.mappers

import com.ashish.movieguide.data.database.entities.Person
import com.ashish.movieguide.data.database.entities.PersonEntity
import com.ashish.movieguide.data.network.entities.common.FullDetailContent
import com.ashish.movieguide.data.network.entities.tmdb.PersonDetail
import com.ashish.movieguide.data.network.entities.trakt.TraktPerson
import com.ashish.movieguide.utils.extensions.convertListToCommaSeparatedText
import io.reactivex.functions.Function
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonDetailMapper @Inject constructor() : Function<FullDetailContent<PersonDetail, TraktPerson>, Person> {

    override fun apply(fullDetailContent: FullDetailContent<PersonDetail, TraktPerson>): Person {
        val personEntity = PersonEntity()
        fullDetailContent.detailContent?.apply {
            personEntity.tmdbId = id!!
            personEntity.name = name
            personEntity.birthday = birthday
            personEntity.biography = biography
            personEntity.homepage = homepage
            personEntity.deathday = deathday
            personEntity.profilePath = profilePath
            personEntity.placeOfBirth = placeOfBirth
            personEntity.profilePath = profilePath

            credits?.cast?.forEach { personEntity.credits?.add(it.toEntity(true)) }
            credits?.crew?.forEach { personEntity.credits?.add(it.toEntity(false)) }

            images?.profiles?.forEach { personEntity.images?.add(it.toEntity()) }

            personEntity.alsoKnownAs = alsoKnownAs.convertListToCommaSeparatedText { it }
        }

        fullDetailContent.traktItem?.addToPersonEntity(personEntity)
        return personEntity
    }
}