package com.ashish.movieguide.data.local.datasource

import com.ashish.movieguide.data.local.entities.LocalShow
import com.ashish.movieguide.data.local.mappers.ShowDetailMapper
import com.ashish.movieguide.data.local.mappers.ShowMapper
import com.ashish.movieguide.data.remote.entities.common.FullDetailContent
import com.ashish.movieguide.data.remote.entities.tmdb.TVShow
import com.ashish.movieguide.data.remote.entities.tmdb.TVShowDetail
import com.ashish.movieguide.data.remote.entities.trakt.TraktShow
import io.reactivex.Observable
import io.requery.Persistable
import io.requery.reactivex.KotlinReactiveEntityStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowLocalDataSource @Inject constructor(
        private val showMapper: ShowMapper,
        private val showDetailMapper: ShowDetailMapper,
        private val dataStore: KotlinReactiveEntityStore<Persistable>
) {

    fun getShow(showId: Long): Observable<LocalShow> {
        return dataStore.findByKey(LocalShow::class, showId).toObservable()
    }

    fun putShow(show: TVShow, traktShow: TraktShow?): Observable<LocalShow> {
        val showEntity = showMapper.apply(show, traktShow)
        return dataStore.upsert(showEntity).toObservable()
    }

    fun putShowDetail(fullShowDetail: FullDetailContent<TVShowDetail, TraktShow>): Observable<LocalShow> {
        val similarMovies = fullShowDetail.detailContent?.similarTVShowResults?.results
        similarMovies?.forEach { putShow(it, null) }

        val showEntity = showDetailMapper.apply(fullShowDetail)
        return dataStore.upsert(showEntity).toObservable()
    }
}