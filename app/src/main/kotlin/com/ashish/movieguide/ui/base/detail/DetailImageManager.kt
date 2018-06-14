package com.ashish.movieguide.ui.base.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.util.Pair
import android.support.v7.widget.RecyclerView
import android.transition.TransitionInflater
import android.transition.TransitionSet
import android.view.View
import android.view.ViewStub
import android.widget.ImageView
import com.ashish.movieguide.R
import com.ashish.movieguide.di.scopes.ActivityScope
import com.ashish.movieguide.ui.common.adapter.ImageAdapter
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.ui.imageviewer.ImageViewerActivity
import com.ashish.movieguide.utils.extensions.find
import com.ashish.movieguide.utils.extensions.inflateToRecyclerView
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.extensions.startActivityWithTransition
import com.ashish.movieguide.utils.transition.LeakFreeSupportSharedElementCallback
import java.util.ArrayList
import javax.inject.Inject

@ActivityScope
class DetailImageManager @Inject constructor(private val activity: Activity) {

    private var reenterState: Bundle? = null
    private var imageAdapter: ImageAdapter? = null
    private var imagesRecyclerView: RecyclerView? = null

    private val callback = object : LeakFreeSupportSharedElementCallback() {
        override fun onMapSharedElements(names: MutableList<String>, sharedElements: MutableMap<String, View>) {
            super.onMapSharedElements(names, sharedElements)
            if (reenterState != null) {
                val currentPosition = reenterState!!.getInt(ImageViewerActivity.EXTRA_CURRENT_POSITION)

                // Locate the ViewHolder for the clicked position.
                val holder = imagesRecyclerView?.findViewHolderForAdapterPosition(currentPosition)
                holder?.itemView?.let {
                    // Map the first shared element name to the child ImageView.
                    sharedElements[names[0]] = it.find(R.id.detail_content_image)
                }

                reenterState = null
            }
        }
    }

    private val onImageItemClickListener = object : OnItemClickListener {
        override fun onItemClick(position: Int, view: View) {
            // Exclude the clicked view from the exit transition (e.g. the clicked view will disappear immediately
            // instead of fading out with the rest to prevent an overlapping animation of fade and move).
            val exitTransition = activity.window.exitTransition
            if (exitTransition is TransitionSet) {
                exitTransition.excludeTarget(view, true)
            }

            val imageUrlList = imageAdapter?.imageUrlList
            if (imageUrlList.isNotNullOrEmpty()) {
                val imageView = view.findViewById(R.id.detail_content_image) as ImageView
                startImageViewerActivity(imageUrlList!!, position, imageView)
            }
        }
    }

    private fun startImageViewerActivity(imageUrlList: ArrayList<String>, position: Int, view: View?) {
        val imagePair = if (view != null) Pair.create(view, "image_$position") else null
        val intent = ImageViewerActivity.createIntent(activity, position, imageUrlList)
        activity.startActivityWithTransition(imagePair, intent)
    }

    fun initImageTransition() {
        activity.window.exitTransition = TransitionInflater.from(activity)
                .inflateTransition(R.transition.grid_exit_transition)

        ActivityCompat.setExitSharedElementCallback(activity, callback)
    }

    fun showImageList(imagesViewStub: ViewStub, imageUrlList: ArrayList<String>) {
        imageAdapter = ImageAdapter(imageUrlList, onImageItemClickListener)
        imagesRecyclerView = imagesViewStub.inflateToRecyclerView(
                activity,
                R.id.detailImagesRecyclerView,
                imageAdapter!!,
                false
        )
    }

    fun fixImagePositionOnActivityReenter(data: Intent) {
        /// Get extras passed to this activity from ImageViewerActivity
        reenterState = data.extras
        val currentPosition = reenterState?.getInt(ImageViewerActivity.EXTRA_CURRENT_POSITION)

        if (currentPosition != null) {
            imagesRecyclerView?.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
                override fun onLayoutChange(view: View, left: Int, top: Int, right: Int, bottom: Int,
                                            oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                    imagesRecyclerView?.removeOnLayoutChangeListener(this)
                    imagesRecyclerView?.layoutManager?.let { lm ->
                        val viewAtPosition = lm.findViewByPosition(currentPosition)
                        // Scroll to position if the view for the current position is null (not currently part of
                        // layout manager children), or it's not completely visible.
                        if (viewAtPosition == null
                                || lm.isViewPartiallyVisible(viewAtPosition, false, true)) {
                            imagesRecyclerView?.smoothScrollToPosition(currentPosition)
                        }
                    }
                }
            })
        }
    }

    fun performCleanup() {
        imageAdapter?.removeListener()
        imagesRecyclerView?.adapter = null
    }
}