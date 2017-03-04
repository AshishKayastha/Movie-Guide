package com.ashish.movieguide.ui.common.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import butterknife.bindView
import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.YouTubeVideo
import com.ashish.movieguide.ui.widget.FontTextView
import com.ashish.movieguide.utils.Constants.THUMBNAIL_SIZE
import com.ashish.movieguide.utils.extensions.applyText
import com.ashish.movieguide.utils.extensions.inflate
import com.bumptech.glide.Glide

class VideoAdapter(
        val youTubeVideos: List<YouTubeVideo>,
        private var onItemClickListener: OnItemClickListener?
) : RecyclerView.Adapter<VideoAdapter.VideoHolder>(), RemoveListener {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VideoHolder {
        return VideoHolder(parent?.inflate(R.layout.list_item_detail_video)!!)
    }

    override fun onBindViewHolder(holder: VideoHolder, position: Int) {
        holder.bindData(youTubeVideos[position])
    }

    override fun getItemCount() = youTubeVideos.size

    override fun onViewRecycled(holder: VideoHolder) {
        super.onViewRecycled(holder)
        Glide.clear(holder.imageView)
    }

    override fun removeListener() {
        onItemClickListener = null
    }

    inner class VideoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView: ImageView by bindView(R.id.detail_content_video)
        private val videoNameText: FontTextView by bindView(R.id.video_name_text)

        init {
            itemView.setOnClickListener { view ->
                onItemClickListener?.onItemClick(adapterPosition, view)
            }
        }

        fun bindData(youTubeVideo: YouTubeVideo) {
            videoNameText.applyText(youTubeVideo.name, true)

            Glide.with(imageView.context)
                    .load(youTubeVideo.imageUrl)
                    .animate(R.anim.fade_in)
                    .override(THUMBNAIL_SIZE, THUMBNAIL_SIZE)
                    .into(imageView)
        }
    }
}