package com.ashish.movieguide.data.local.datasource

import com.ashish.movieguide.data.local.entities.LocalPerson
import com.ashish.movieguide.data.local.mappers.PersonDetailMapper
import com.ashish.movieguide.data.local.mappers.PersonMapper
import com.ashish.movieguide.data.remote.entities.common.FullDetailContent
import com.ashish.movieguide.data.remote.entities.tmdb.Person
import com.ashish.movieguide.data.remote.entities.tmdb.PersonDetail
import com.ashish.movieguide.data.remote.entities.trakt.TraktPerson
import io.reactivex.Observable
import io.requery.Persistable
import io.requery.reactivex.KotlinReactiveEntityStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonLocalDataSource @Inject constructor(
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