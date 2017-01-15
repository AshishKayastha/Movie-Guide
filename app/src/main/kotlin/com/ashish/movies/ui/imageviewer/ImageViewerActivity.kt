package com.ashish.movies.ui.imageviewer

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.SharedElementCallback
import android.support.v4.view.ViewPager
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.ui.base.common.BaseActivity
import com.ashish.movies.ui.widget.DepthPageTransformer
import com.ashish.movies.utils.extensions.changeViewGroupTextFont
import com.ashish.movies.utils.systemuihelper.SystemUiHelper
import icepick.State
import java.util.*

/**
 * Created by Ashish on Jan 08.
 */
class ImageViewerActivity : BaseActivity() {

    @JvmField @State var title: String = ""
    @JvmField @State var currentPosition: Int = 0
    @JvmField @State var startingPosition: Int = 0
    @JvmField @State var imageUrlList: ArrayList<String>? = null

    var systemUiHelper: SystemUiHelper? = null

    private val viewPager: ViewPager by bindView(R.id.view_pager)
    private val appbarLayout: FrameLayout by bindView(R.id.app_bar)
    private val wrapperLayout: LinearLayout by bindView(R.id.appbar_wrapper)

    private var isReturning: Boolean = false
    private lateinit var imageViewerAdapter: ImageViewerAdapter

    private val callback = object : SharedElementCallback() {
        override fun onMapSharedElements(names: MutableList<String>, sharedElements: MutableMap<String, View>) {
            if (isReturning) {
                val sharedElement = imageViewerAdapter.getRegisteredFragment(currentPosition)?.getImageView()
                if (sharedElement == null) {
                    names.clear()
                    sharedElements.clear()
                } else if (startingPosition != currentPosition) {
                    names.clear()
                    names.add(sharedElement.transitionName)
                    sharedElements.clear()
                    sharedElements.put(sharedElement.transitionName, sharedElement)
                }
            }
        }
    }

    private val visibilityChangeListener = object : SystemUiHelper.OnVisibilityChangeListener {
        override fun onVisibilityChange(visible: Boolean) {
            wrapperLayout.animate()
                    .alpha(if (visible) 1f else 0f)
                    .translationY(if (visible) 0f else -appbarLayout.bottom.toFloat())
                    .setDuration(400L)
                    .setInterpolator(FastOutSlowInInterpolator())
                    .start()
        }
    }

    companion object {
        const val SHOW_UI_MILLIS = 4000L
        const val EXTRA_CURRENT_POSITION = "current_position"
        const val EXTRA_STARTING_POSITION = "starting_position"

        private const val EXTRA_TITLE = "title"
        private const val EXTRA_IMAGE_URL_LIST = "image_url_list"

        fun createIntent(context: Context, title: String, startingPosition: Int,
                         imageUrlList: ArrayList<String>): Intent {
            return Intent(context, ImageViewerActivity::class.java)
                    .putExtra(EXTRA_TITLE, title)
                    .putExtra(EXTRA_IMAGE_URL_LIST, imageUrlList)
                    .putExtra(EXTRA_STARTING_POSITION, startingPosition)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        setEnterSharedElementCallback(callback)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            if (this@ImageViewerActivity.title.isEmpty()) {
                setImageCountTitle()
            } else {
                title = this@ImageViewerActivity.title
            }
        }

        toolbar?.changeViewGroupTextFont()

        imageViewerAdapter = ImageViewerAdapter(supportFragmentManager, imageUrlList!!, startingPosition)
        viewPager.apply {
            adapter = imageViewerAdapter
            currentItem = currentPosition
            setPageTransformer(true, DepthPageTransformer())
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                currentPosition = position
                setImageCountTitle()
            }

            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        })

        systemUiHelper = SystemUiHelper(this, listener = visibilityChangeListener)
    }

    override fun getIntentExtras(extras: Bundle?) {
        title = extras?.getString(EXTRA_TITLE) ?: ""
        startingPosition = extras?.getInt(EXTRA_STARTING_POSITION) ?: 0
        currentPosition = startingPosition
        imageUrlList = extras?.getStringArrayList(EXTRA_IMAGE_URL_LIST)
    }

    private fun setImageCountTitle() {
        if (title.isEmpty()) {
            supportActionBar?.title = "${currentPosition + 1} of ${imageUrlList!!.size}"
        }
    }

    override fun getLayoutId() = R.layout.activity_image_viewer

    override fun onStart() {
        super.onStart()
        systemUiHelper?.show()
        systemUiHelper?.delayHide(SHOW_UI_MILLIS)
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }

    override fun supportFinishAfterTransition() {
        isReturning = true
        val data = Intent()
        data.putExtra(EXTRA_CURRENT_POSITION, currentPosition)
        data.putExtra(EXTRA_STARTING_POSITION, startingPosition)
        setResult(Activity.RESULT_OK, data)
        super.supportFinishAfterTransition()
    }

    override fun onDestroy() {
        viewPager.clearOnPageChangeListeners()
        systemUiHelper?.removeVisibilityChangeListener()
        super.onDestroy()
    }
}