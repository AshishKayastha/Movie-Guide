package com.ashish.movies.ui.common.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.utils.Constants.THUMBNAIL_SIZE
import com.ashish.movies.utils.extensions.inflate
import com.bumptech.glide.Glide
import java.util.*

/**
 * Created by Ashish on Jan 14.
 */
class ImageAdapter(val imageUrlList: ArrayList<String>, var onItemClickListener: OnItemClickListener?)
    : RecyclerView.Adapter<ImageAdapter.ImageHolder>(), RemoveListener {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ImageHolder {
        return ImageHolder(parent?.inflate(R.layout.list_item_detail_image)!!)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.bindData(imageUrlList[position], position)
    }

    override fun getItemCount() = imageUrlList.size

    override fun onViewRecycled(holder: ImageHolder) {
        super.onViewRecycled(holder)
        Glide.clear(holder.imageView)
    }

    override fun removeListener() {
        onItemClickListener = null
    }

    inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView: ImageView by bindView(R.id.detail_content_image)

        fun bindData(imageUrl: String, position: Int) {
            Glide.with(imageView.context)
                    .load(imageUrl)
                    .asBitmap()
                    .override(THUMBNAIL_SIZE, THUMBNAIL_SIZE)
                    .into(imageView)

            imageView.transitionName = "image_$position"
            itemView.setOnClickListener { onItemClickListener?.onItemClick(position, it) }
        }
    }
}