package com.ashish.movies.ui.common.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.ui.widget.FontTextView
import com.ashish.movies.utils.extensions.inflate
import com.ashish.movies.utils.extensions.isNotNullOrEmpty
import com.ashish.movies.utils.extensions.setVisibility

/**
 * Created by Ashish on Jan 11.
 */
class DetailContentAdapter(private val detailContentTitles: Array<String>, private var detailContentList: List<String>)
    : RecyclerView.Adapter<DetailContentAdapter.DetailContentHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DetailContentHolder {
        return DetailContentHolder(parent?.inflate(R.layout.list_item_detail_content)!!)
    }

    override fun onBindViewHolder(holder: DetailContentHolder, position: Int) {
        holder.bindData(detailContentTitles[position], detailContentList[position])
    }

    override fun getItemCount() = detailContentList.size

    inner class DetailContentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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