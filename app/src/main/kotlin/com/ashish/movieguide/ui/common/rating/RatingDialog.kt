package com.ashish.movieguide.ui.common.rating

import android.content.Context
import android.support.v7.app.AlertDialog
import android.widget.RatingBar
import com.ashish.movieguide.R
import com.ashish.movieguide.di.scopes.ActivityScope
import com.ashish.movieguide.ui.widget.FontTextView
import com.ashish.movieguide.utils.extensions.changeDialogButtonTypeface
import com.ashish.movieguide.utils.extensions.enableNegativeButton
import com.ashish.movieguide.utils.extensions.enablePositiveButton
import com.ashish.movieguide.utils.extensions.find
import com.ashish.movieguide.utils.extensions.inflateLayout
import javax.inject.Inject

@ActivityScope
class RatingDialog @Inject constructor(private val context: Context) {

    private var ratingDialog: AlertDialog? = null
    private var listener: UpdateRatingListener? = null

    fun setRatingListener(listener: UpdateRatingListener?) {
        this.listener = listener
    }

    fun showRatingDialog(rating: Double?) {
        val view = context.inflateLayout(R.layout.dialog_rating)

        val ratingBar = view.find<RatingBar>(R.id.rating_bar)
        val floatRating = rating?.toFloat() ?: 0F
        ratingBar.rating = floatRating

        val ratingText = view.find<FontTextView>(R.id.rating_value_text)
        setRatingText(ratingText, floatRating)

        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            setRatingText(ratingText, rating)
            ratingDialog?.enablePositiveButton(rating > 0)
        }

        ratingDialog = AlertDialog.Builder(context, R.style.AlertDialogTheme)
                .setCancelable(false)
                .setView(view)
                .setNeutralButton(android.R.string.cancel, null)
                .setNegativeButton(R.string.remove_btn, { _, _ -> listener?.removeRating() })
                .setPositiveButton(R.string.rate_btn, { _, _ -> listener?.addRating(ratingBar.rating.toInt()) })
                .create()

        ratingDialog?.changeDialogButtonTypeface()
        ratingDialog!!.show()

        val isValidRating = ratingBar.rating > 0
        ratingDialog?.enablePositiveButton(isValidRating)
        ratingDialog?.enableNegativeButton(isValidRating)
    }

    private fun setRatingText(ratingText: FontTextView, rating: Float) {
        ratingText.text = if (rating > 0) rating.toInt().toString() else ""
    }

    fun dismissDialog() {
        ratingDialog?.dismiss()
    }

    interface UpdateRatingListener {
        fun addRating(rating: Int)
        fun removeRating()
    }
}