package com.ashish.movieguide.ui.base.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.util.Pair
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewStub
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.ashish.movieguide.R
import com.ashish.movieguide.di.scopes.ActivityScope
import com.ashish.movieguide.ui.common.adapter.ImageAdapter
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.ui.imageviewer.ImageViewerActivity
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
        override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String, View>?) {
            super.onMapSharedElements(names, sharedElements)

            if (reenterState != null) {
                val currentPosition = reenterState!!.getInt(ImageViewerActivity.EXTRA_CURRENT_POSITION)
                val startingPosition = reenterState!!.getInt(ImageViewerActivity.EXTRA_STARTING_POSITION)

                /*
                  If startingPosition != currentPosition the user must have swiped to a
                  different page in the ImageViewerActivity. We must update the shared element
                  so that the correct one falls into place.
                 */
                if (startingPosition != currentPosition) {
                    val newSharedElement = imagesRecyclerView?.layoutManager?.findViewByPosition(currentPosition)
                    if (newSharedElement != null) {
                        val newTransitionName = "image_$currentPosition"
                        names?.clear()
                        sharedElements?.clear()
                        names?.add(newTransitionName)
                        sharedElements?.put(newTransitionName, newSharedElement)
                    }
                }

                reenterState = null
            }
        }
    }

    private val onImageItemClickListener = object : OnItemClickListener {
        override fun onItemClick(position: Int, view: View) {
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
        ActivityCompat.setExitSharedElementCallback(activity, callback)
    }

    fun showImageList(imagesViewStub: ViewStub, imageUrlList: ArrayList<String>) {
        imageAdapter = ImageAdapter(imageUrlList, onImageItemClickListener)
        imagesRecyclerView = imagesViewStub.inflateToRecyclerView(
                activity,
                R.id.detailImagesRecyclerView,
                imageAdapter!!
        )
    }

    fun fixImagePositionOnActivityReenter(data: Intent) {
        // Get extras passed to this activity from ImageViewerActivity
        reenterState = data.extras
        val currentPosition = reenterState?.getInt(ImageViewerActivity.EXTRA_CURRENT_POSITION)
        val startingPosition = reenterState?.getInt(ImageViewerActivity.EXTRA_STARTING_POSITION)

        /*
          If startingPosition and currentPosition are not same
          then scroll images recyclerview to currentPosition
         */
        if (startingPosition != currentPosition) {
            imagesRecyclerView?.smoothScrollToPosition(currentPosition!!)
        }

        /*
          Postpone reenter transition as the image recyclerview
          may not have been drawn by this time so we want to delay the
          transition until view is drawn.
         */
        activity.postponeEnterTransition()
        imagesRecyclerView?.viewTreeObserver?.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                imagesRecyclerView?.viewTreeObserver?.removeOnPreDrawListener(this)

                // Fix required for smooth transition
                imagesRecyclerView?.requestLayout()

                /*
                  Start postponed shared element transiton when we know
                  that recyclerview is now drawn.
                 */
                activity.startPostponedEnterTransition()
                return true
            }
        })
    }

    fun performCleanup() {
        imageAdapter?.removeListener()
        imagesRecyclerView?.adapter = null
    }
}