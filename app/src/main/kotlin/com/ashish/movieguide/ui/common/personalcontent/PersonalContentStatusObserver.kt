package com.ashish.movieguide.ui.common.personalcontent

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This will notify a movie or tv show has been removed from favorites or watchlist.
 * This is useful when we need to remove particular movie or tv show from a list
 * when the status is changed in detail page.
 */
@Singleton
class PersonalContentStatusObserver @Inject constructor() {

    private val contentStatusChangeSubject = PublishSubject.create<Long>()

    fun getContentStatusObservable(): Observable<Long> = contentStatusChangeSubject.hide()

    fun notifyContentRemoved(mediaId: Long) = contentStatusChangeSubject.onNext(mediaId)
}