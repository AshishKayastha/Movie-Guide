package com.ashish.movies.utils.extensions

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ashish.movies.R
import com.ashish.movies.data.api.OMDbApi
import com.ashish.movies.data.models.FullDetailContent
import com.ashish.movies.data.models.OMDbDetail
import com.ashish.movies.utils.Constants.NOT_AVAILABLE
import io.reactivex.Observable

/**
 * Created by Ashish on Jan 09.
 */
fun <I> OMDbApi.convertToFullDetailContent(imdbId: String?, detailContent: I): Observable<FullDetailContent<I>> {
    if (imdbId.isNotNullOrEmpty()) {
        return getDetailFromIMDbId(imdbId!!)
                .flatMap({ getFullDetailContent(detailContent, it) })
                .onErrorReturnItem(FullDetailContent(detailContent, null))
    } else {
        return getFullDetailContent(detailContent, null)
    }
}

private fun <I> getFullDetailContent(detailContent: I, omdbDetail: OMDbDetail?): Observable<FullDetailContent<I>> {
    return Observable.just(FullDetailContent(detailContent, omdbDetail))
}

fun OMDbDetail.setIMDbRating(imdbRatingView: View, imdbRatingText: TextView) {
    if (imdbRating.isNotNullOrEmpty() && imdbRating != NOT_AVAILABLE) {
        imdbRatingView.show()
        imdbRatingText.text = imdbRating
    }
}

fun OMDbDetail.setTomatoRating(tomatoRatingView: View, tomatoRatingText: TextView, tomatoRatingImage: ImageView) {
    if (tomatoRating.isNotNullOrEmpty() && tomatoRating != NOT_AVAILABLE) {
        tomatoRatingView.show()
        tomatoRatingText.text = tomatoRating

        if (tomatoImage == "certified") {
            tomatoRatingImage.setImageResource(R.drawable.ic_rt_certified)
        } else if (tomatoImage == "fresh") {
            tomatoRatingImage.setImageResource(R.drawable.ic_rt_fresh)
        } else if (tomatoImage == "rotten") {
            tomatoRatingImage.setImageResource(R.drawable.ic_rt_rotten)
        }
    }
}

fun OMDbDetail.setFlixterScore(flixterScoreView: View, flixterScoreText: TextView, flixterScoreImage: ImageView) {
    if (tomatoUserRating.isNotNullOrEmpty() && tomatoUserRating != NOT_AVAILABLE) {
        flixterScoreView.show()
        flixterScoreText.text = tomatoUserRating

        val flixterScore = tomatoUserRating?.toFloat() ?: 0f
        if (flixterScore > 3.4) {
            flixterScoreImage.setImageResource(R.drawable.ic_flixter_good)
        } else {
            flixterScoreImage.setImageResource(R.drawable.ic_flixter_bad)
        }
    }
}

fun OMDbDetail.setMetaScore(metascoreView: View, metascoreText: TextView) {
    if (Metascore.isNotNullOrEmpty() && Metascore != NOT_AVAILABLE) {
        metascoreView.show()
        metascoreText.text = Metascore

        val metaScoreInt = Metascore?.toInt() ?: 0
        if (metaScoreInt > 60) {
            metascoreText.setBackgroundColor(Color.parseColor("#66CC33"))
        } else if (metaScoreInt > 40 && metaScoreInt < 61) {
            metascoreText.setBackgroundColor(Color.parseColor("#FFCC33"))
        } else {
            metascoreText.setBackgroundColor(Color.parseColor("#FF0000"))
        }
    }
}