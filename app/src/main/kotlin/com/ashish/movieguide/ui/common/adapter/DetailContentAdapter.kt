package com.ashish.movieguide.ui.common.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.ashish.movieguide.R
import com.ashish.movieguide.ui.widget.FontTextView
import com.ashish.movieguide.utils.extensions.bindView
import com.ashish.movieguide.utils.extensions.inflate
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.extensions.setVisibility

/**
 * Created by Ashish on Jan 11.
 */
class DetailContentAdapter(
        private val detailContentTitles: Array<String>,
        private var detailContentList: List<String>
) : RecyclerView.Adapter<DetailContentAdapter.DetailContentHolder>() {

    companion object {
        private const val SINGLE_ROW_VIEW_TYPE = 0
        private const val MULTI_ROW_VIEW_TYPE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailContentHolder {
        val layoutId = if (viewType == SINGLE_ROW_VIEW_TYPE) {
            R.layout.list_item_detail_content
        } else {
            R.layout.list_item_detail_content_alt
        }

        return DetailContentHolder(parent.inflate(layoutId)!!)
    }

    override fun onBindViewHolder(holder: DetailContentHolder, position: Int) {
        holder.bindData(detailContentTitles[position], detailContentList[position])
    }

    override fun getItemCount() = detailContentList.size

    override fun getItemViewType(position: Int): Int {
        return if (detailContentList[position].length < 30) SINGLE_ROW_VIEW_TYPE else MULTI_ROW_VIEW_TYPE
    }

    class DetailContentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val contentText: FontTextView by bindView(R.id.content_text)
        private val contentTitle: FontTextView by bindView(R.id.content_title)

        fun bindData(title: String, text: String) {
            val isValidText = text.isNotNullOrEmpty() && text != "N/A"
            contentText.setVisibility(isValidText)
            contentTitle.setVisibility(isValidText)
            if (isValidText) {
                contentText.text = text
                contentTitle.text = title
            }
        }
    }
}