package com.ashish.movieguide.ui.common.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.ashish.movieguide.R
import com.ashish.movieguide.data.network.entities.tmdb.YouTubeVideo
import com.ashish.movieguide.ui.base.recyclerview.BaseHolder
import com.ashish.movieguide.ui.widget.FontTextView
import com.ashish.movieguide.utils.extensions.applyText
import com.ashish.movieguide.utils.extensions.bindView
import com.ashish.movieguide.utils.extensions.inflate
import com.ashish.movieguide.utils.extensions.loadImage
import com.ashish.movieguide.utils.glide.GlideApp

class VideoAdapter(
        val youTubeVideos: List<YouTubeVideo>,
        private var onItemClickListener: OnItemClickListener?
) : RecyclerView.Adapter<VideoAdapter.VideoHolder>(), RemoveListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoHolder {
        return VideoHolder(parent.inflate(R.layout.list_item_detail_video))
                .also { it.attachListener(onItemClickListener) }
    }

    override fun onBindViewHolder(holder: VideoHolder, position: Int) {
        holder.bindData(youTubeVideos[position])
    }

    override fun getItemCount() = youTubeVideos.size

    override fun onViewRecycled(holder: VideoHolder) {
        super.onViewRecycled(holder)
        GlideApp.with(holder.imageView.context).clear(holder.imageView)
    }

    override fun removeListener() {
        onItemClickListener = null
    }

    class VideoHolder(view: View) : BaseHolder(view) {

        val imageView: ImageView by bindView(R.id.detail_content_video)
        private val videoNameText: FontTextView by bindView(R.id.video_name_text)

        fun bindData(youTubeVideo: YouTubeVideo) {
            imageView.loadImage(youTubeVideo.imageUrl)
            videoNameText.applyText(youTubeVideo.name, true)
        }
    }
}