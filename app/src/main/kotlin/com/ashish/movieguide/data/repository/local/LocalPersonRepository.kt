package com.ashish.movieguide.data.repository.local

import com.ashish.movieguide.data.database.mappers.PersonDetailMapper
import com.ashish.movieguide.data.database.mappers.PersonMapper
import com.ashish.movieguide.data.network.entities.common.FullDetailContent
import com.ashish.movieguide.data.network.entities.tmdb.Person
import com.ashish.movieguide.data.network.entities.tmdb.PersonDetail
import com.ashish.movieguide.data.network.entities.trakt.TraktPerson
import io.reactivex.Observable
import io.requery.Persistable
import io.requery.reactivex.KotlinReactiveEntityStore
import javax.inject.Inject
import javax.inject.Singleton
import com.ashish.movieguide.data.database.entities.Person as LocalPerson

@Singleton
class LocalPersonRepository @Inject constructor(
        private val personMapper: PersonMapper,
        private val personDetailMapper: PersonDetailMapper,
        private val dataStore: KotlinReactiveEntityStore<Persistable>
) {

    fun getPerson(personId: Long): Observable<LocalPerson> {
        return dataStore.findByKey(LocalPerson::class, personId).toObservable()
    }

    fun putPerson(person: Person, traktPerson: TraktPerson?): Observable<LocalPerson> {
        val personEntity = personMapper.apply(person, traktPerson)
        return dataStore.upsert(personEntity).toObservable()
    }

    fun putPersonBlocking(person: Person, traktPerson: TraktPerson?): LocalPerson {
        val personEntity = personMapper.apply(person, traktPerson)
        return dataStore.upsert(personEntity).blockingGet()
    }

    fun putPersonDetail(fullPersonDetail: FullDetailContent<PersonDetail, TraktPerson>): Observable<LocalPerson> {
        val personEntity = personDetailMapper.apply(fullPersonDetail)
        return dataStore.upsert(personEntity).toObservable()
    }
}