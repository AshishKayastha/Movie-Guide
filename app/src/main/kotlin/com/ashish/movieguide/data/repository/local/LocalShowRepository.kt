package com.ashish.movieguide.data.repository.local

import com.ashish.movieguide.data.database.entities.Show
import com.ashish.movieguide.data.database.mappers.ShowDetailMapper
import com.ashish.movieguide.data.database.mappers.ShowMapper
import com.ashish.movieguide.data.network.entities.common.FullDetailContent
import com.ashish.movieguide.data.network.entities.tmdb.TVShow
import com.ashish.movieguide.data.network.entities.tmdb.TVShowDetail
import com.ashish.movieguide.data.network.entities.trakt.TraktShow
import io.reactivex.Observable
import io.requery.Persistable
import io.requery.reactivex.KotlinReactiveEntityStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalShowRepository @Inject constructor(
        private val showMapper: ShowMapper,
        private val showDetailMapper: ShowDetailMapper,
        private val dataStore: KotlinReactiveEntityStore<Persistable>
) {

    fun getShow(showId: Long): Observable<Show> {
        return dataStore.findByKey(Show::class, showId).toObservable()
    }

    fun putShow(show: TVShow, traktShow: TraktShow?): Observable<Show> {
        val showEntity = showMapper.apply(show, traktShow)
        return dataStore.upsert(showEntity).toObservable()
    }

    fun putShowDetail(fullShowDetail: FullDetailContent<TVShowDetail, TraktShow>): Observable<Show> {
        val similarMovies = fullShowDetail.detailContent?.similarTVShowResults?.results
        similarMovies?.forEach { putShow(it, null) }

        val showEntity = showDetailMapper.apply(fullShowDetail)
        return dataStore.upsert(showEntity).toObservable()
    }
}