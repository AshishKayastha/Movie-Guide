package com.ashish.movieguide.ui.review

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.ashish.movieguide.R
import com.ashish.movieguide.data.remote.entities.tmdb.Review
import com.ashish.movieguide.ui.base.adapter.BaseHolder
import com.ashish.movieguide.ui.base.adapter.ContentDelegateAdapter
import com.ashish.movieguide.ui.base.adapter.RecyclerViewItem
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.ui.widget.CircularTextDrawable
import com.ashish.movieguide.ui.widget.FontTextView
import com.ashish.movieguide.utils.extensions.applyText
import com.ashish.movieguide.utils.extensions.bindView
import com.ashish.movieguide.utils.extensions.hide
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.extensions.show

class ReviewDelegateAdapter(
        layoutId: Int,
        onItemClickListener: OnItemClickListener?
) : ContentDelegateAdapter(layoutId, onItemClickListener) {

    override fun getHolder(view: View) = ReviewHolder(view)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: RecyclerViewItem) {
        (holder as ReviewHolder).bindData(item as Review)
    }

    class ReviewHolder(view: View) : BaseHolder(view) {

        private val reviewAuthorText: FontTextView by bindView(R.id.review_author)
        private val reviewContentText: FontTextView by bindView(R.id.review_content)
        private val reviewAuthorIcon: ImageView  by bindView(R.id.review_author_icon)

        fun bindData(review: Review) {
            with(review) {
                if (author.isNotNullOrEmpty()) {
                    reviewAuthorIcon.show()
                    val drawable = CircularTextDrawable(itemView.context, author!!.first().toString())
                    reviewAuthorIcon.setImageDrawable(drawable)
                } else {
                    reviewAuthorIcon.hide()
                }

                reviewAuthorText.applyText(author)
                reviewContentText.applyText(content)
            }
        }
    }
}