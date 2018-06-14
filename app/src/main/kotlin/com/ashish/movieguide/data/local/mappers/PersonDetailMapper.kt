package com.ashish.movieguide.data.local.mappers

import com.ashish.movieguide.data.local.entities.LocalPerson
import com.ashish.movieguide.data.local.entities.PersonEntity
import com.ashish.movieguide.data.remote.entities.common.FullDetailContent
import com.ashish.movieguide.data.remote.entities.tmdb.PersonDetail
import com.ashish.movieguide.data.remote.entities.trakt.TraktPerson
import com.ashish.movieguide.utils.extensions.convertListToCommaSeparatedText
import io.reactivex.functions.Function
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonDetailMapper @Inject constructor()
    : Function<FullDetailContent<PersonDetail, TraktPerson>, LocalPerson> {

    override fun apply(fullDetailContent: FullDetailContent<PersonDetail, TraktPerson>): LocalPerson {
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