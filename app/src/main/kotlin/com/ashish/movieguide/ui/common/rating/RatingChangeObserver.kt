package com.ashish.movieguide.ui.common.rating

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RatingChangeObserver @Inject constructor() {

    private val ratingChangeSubject = PublishSubject.create<Pair<Long, Int>>()

    fun getRatingObservable(): Observable<Pair<Long, Int>> = ratingChangeSubject.hide()

    fun notifyRatingChanged(mediaId: Long, rating: Int) {
        ratingChangeSubject.onNext(Pair(mediaId, rating))
    }
}