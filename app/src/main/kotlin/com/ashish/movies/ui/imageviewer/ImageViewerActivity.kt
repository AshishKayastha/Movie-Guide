package com.ashish.movies.ui.imageviewer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.ui.base.common.BaseActivity
import com.ashish.movies.ui.widget.DepthPageTransformer
import com.ashish.movies.utils.extensions.changeViewGroupTextFont
import icepick.State
import java.util.*

/**
 * Created by Ashish on Jan 08.
 */
class ImageViewerActivity : BaseActivity() {

    private val viewPager: ViewPager by bindView(R.id.view_pager)

    @JvmField @State var title: String = ""
    @JvmField @State var currentPosition: Int = 0
    @JvmField @State var imageUrlList: ArrayList<String>? = null

    companion object {
        private const val EXTRA_TITLE = "title"
        private const val EXTRA_IMAGE_URL_LIST = "image_url_list"
        private const val EXTRA_CURRENT_POSITION = "current_position"

        fun createIntent(context: Context, title: String, currentPosition: Int,
                         imageUrlList: ArrayList<String>): Intent {
            return Intent(context, ImageViewerActivity::class.java)
                    .putExtra(EXTRA_TITLE, title)
                    .putExtra(EXTRA_IMAGE_URL_LIST, imageUrlList)
                    .putExtra(EXTRA_CURRENT_POSITION, currentPosition)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            if (this@ImageViewerActivity.title.isEmpty()) {
                setImageCountTitle()
            } else {
                title = this@ImageViewerActivity.title
            }
        }

        toolbar?.changeViewGroupTextFont()

        viewPager.apply {
            adapter = ImageViewerAdapter(supportFragmentManager, imageUrlList!!)
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
    }

    override fun getIntentExtras(extras: Bundle?) {
        title = extras?.getString(EXTRA_TITLE) ?: ""
        currentPosition = extras?.getInt(EXTRA_CURRENT_POSITION) ?: 0
        imageUrlList = extras?.getStringArrayList(EXTRA_IMAGE_URL_LIST)
    }

    private fun setImageCountTitle() {
        if (title.isEmpty()) {
            supportActionBar?.title = "${currentPosition + 1} of ${imageUrlList!!.size}"
        }
    }

    override fun getLayoutId() = R.layout.activity_image_viewer

    override fun onDestroy() {
        viewPager.clearOnPageChangeListeners()
        super.onDestroy()
    }
}