package com.ashish.movies.ui.imageviewer

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.Transition
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.ui.base.common.BaseFragment
import com.ashish.movies.ui.widget.TouchImageView
import com.ashish.movies.utils.Constants.THUMBNAIL_SIZE
import com.ashish.movies.utils.SystemUiHelper
import com.ashish.movies.utils.TransitionListenerAdapter
import com.bumptech.glide.BitmapRequestBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import icepick.State

/**
 * Created by Ashish on Jan 08.
 */
class ImageViewerFragment : BaseFragment() {

    companion object {
        private const val ARG_POSITION = "position"
        private const val ARG_IMAGE_URL = "image_url"

        fun newInstance(position: Int, imageUrl: String?): ImageViewerFragment {
            val args = Bundle()
            args.putInt(ARG_POSITION, position)
            args.putString(ARG_IMAGE_URL, imageUrl)
            val fragment = ImageViewerFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @JvmField @State var position: Int = 0
    @JvmField @State var imageUrl: String? = null

    private val imageView: TouchImageView by bindView(R.id.image_view)

    private var fullBitmap: BitmapRequestBuilder<String, Bitmap>? = null
    private var thumbBitmap: BitmapRequestBuilder<String, Bitmap>? = null

    override fun getLayoutId() = R.layout.fragment_image_viewer

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGlide()
        setupImage()
        handleTouchEvent()
    }

    override fun getFragmentArguments(arguments: Bundle?) {
        imageUrl = arguments?.getString(ARG_IMAGE_URL)
        position = arguments?.getInt(ARG_POSITION) ?: 0
    }

    private fun setupGlide() {
        thumbBitmap = Glide.with(this)
                .load(imageUrl)
                .asBitmap()
                .dontAnimate()
                .priority(Priority.IMMEDIATE)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .override(THUMBNAIL_SIZE, THUMBNAIL_SIZE)

        fullBitmap = Glide.with(this)
                .load(imageUrl)
                .asBitmap()
                .override(Resources.getSystem().displayMetrics.widthPixels, Target.SIZE_ORIGINAL)
    }

    fun loadThumbnail(startTransition: Boolean) {
        thumbBitmap?.into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(bitmap: Bitmap?, animation: GlideAnimation<in Bitmap>?) {
                if (bitmap != null) imageView.setImageBitmap(bitmap)
                if (startTransition) activity?.startPostponedEnterTransition()
            }

            override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                if (startTransition) activity?.startPostponedEnterTransition()
            }
        })
    }

    private fun loadFullImage() {
        fullBitmap?.thumbnail(thumbBitmap)?.into(imageView)
    }

    private fun setupImage() {
        imageView.transitionName = "image_$position"

        activity?.window?.sharedElementEnterTransition?.addListener(object : TransitionListenerAdapter() {
            override fun onTransitionEnd(transition: Transition) {
                activity?.window?.sharedElementEnterTransition?.removeListener(this)
                loadFullImage()
            }
        })

        loadThumbnail(true)
        fullBitmap?.preload()
    }

    fun getImageView(): ImageView? {
        return if (isViewInBounds(activity.window.decorView, imageView)) imageView else null
    }

    private fun isViewInBounds(container: View, view: View): Boolean {
        val containerBounds = Rect()
        container.getHitRect(containerBounds)
        return view.getLocalVisibleRect(containerBounds)
    }

    private fun handleTouchEvent() {
        if (activity != null) {
            val detector = GestureDetector(activity, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDown(event: MotionEvent) = true

                override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
                    toggleSystemUiVisibility()
                    return true
                }
            })

            imageView.setOnTouchListener { view, event -> detector.onTouchEvent(event) }
        }
    }

    private fun toggleSystemUiVisibility() {
        val systemUiHelper = getSystemUiHelper()
        if (systemUiHelper != null) {
            if (systemUiHelper.isShowing) {
                systemUiHelper.hide()
            } else {
                systemUiHelper.show()
                systemUiHelper.delayHide(ImageViewerActivity.SHOW_UI_MILLIS)
            }
        }
    }

    private fun getSystemUiHelper(): SystemUiHelper? {
        return if (activity != null) (activity as ImageViewerActivity).systemUiHelper else null
    }

    override fun onDestroyView() {
        Glide.clear(imageView)
        imageView.setOnTouchListener(null)
        super.onDestroyView()
    }
}