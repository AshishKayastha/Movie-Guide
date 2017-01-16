package com.ashish.movies.ui.base.detail

import android.animation.Animator
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.SharedElementCallback
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.Transition
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewStub
import android.view.ViewTreeObserver
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import butterknife.bindOptionalView
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.data.models.Credit
import com.ashish.movies.ui.base.mvp.MvpActivity
import com.ashish.movies.ui.common.adapter.DetailContentAdapter
import com.ashish.movies.ui.common.adapter.ImageAdapter
import com.ashish.movies.ui.common.adapter.OnItemClickListener
import com.ashish.movies.ui.common.adapter.RecyclerViewAdapter
import com.ashish.movies.ui.common.adapter.ViewType
import com.ashish.movies.ui.common.palette.PaletteBitmap
import com.ashish.movies.ui.imageviewer.ImageViewerActivity
import com.ashish.movies.ui.imageviewer.ImageViewerActivity.Companion.EXTRA_CURRENT_POSITION
import com.ashish.movies.ui.imageviewer.ImageViewerActivity.Companion.EXTRA_STARTING_POSITION
import com.ashish.movies.ui.widget.FontTextView
import com.ashish.movies.ui.widget.ItemOffsetDecoration
import com.ashish.movies.utils.Constants
import com.ashish.movies.utils.Constants.ADAPTER_TYPE_CREDIT
import com.ashish.movies.utils.Constants.ADAPTER_TYPE_EPISODE
import com.ashish.movies.utils.Constants.ADAPTER_TYPE_MOVIE
import com.ashish.movies.utils.Constants.ADAPTER_TYPE_PERSON
import com.ashish.movies.utils.Constants.ADAPTER_TYPE_SEASON
import com.ashish.movies.utils.Constants.ADAPTER_TYPE_TV_SHOW
import com.ashish.movies.utils.Constants.IMDB_BASE_URL
import com.ashish.movies.utils.CustomTypefaceSpan
import com.ashish.movies.utils.FontUtils
import com.ashish.movies.utils.GravitySnapHelper
import com.ashish.movies.utils.TransitionListenerAdapter
import com.ashish.movies.utils.Utils
import com.ashish.movies.utils.extensions.animateBackgroundColorChange
import com.ashish.movies.utils.extensions.animateColorChange
import com.ashish.movies.utils.extensions.animateTextColorChange
import com.ashish.movies.utils.extensions.changeMenuFont
import com.ashish.movies.utils.extensions.getActivityOptionsCompat
import com.ashish.movies.utils.extensions.getColorCompat
import com.ashish.movies.utils.extensions.getOverflowMenuButton
import com.ashish.movies.utils.extensions.getPosterImagePair
import com.ashish.movies.utils.extensions.getSwatchWithMostPixels
import com.ashish.movies.utils.extensions.hide
import com.ashish.movies.utils.extensions.isDark
import com.ashish.movies.utils.extensions.isNotNullOrEmpty
import com.ashish.movies.utils.extensions.loadPaletteBitmap
import com.ashish.movies.utils.extensions.scrimify
import com.ashish.movies.utils.extensions.setLightStatusBar
import com.ashish.movies.utils.extensions.setPaletteColor
import com.ashish.movies.utils.extensions.setTransitionName
import com.ashish.movies.utils.extensions.show
import java.util.*

/**
 * Created by Ashish on Jan 03.
 */
abstract class BaseDetailActivity<I, V : BaseDetailView<I>, P : BaseDetailPresenter<I, V>>
    : MvpActivity<V, P>(), BaseDetailView<I>, AppBarLayout.OnOffsetChangedListener {

    protected val titleText: FontTextView by bindView(R.id.content_title_text)
    protected val posterImage: ImageView by bindView(R.id.detail_poster_image)
    private val playTrailerFAB: FloatingActionButton? by bindOptionalView(R.id.play_trailer_fab)

    private val tabLayout: TabLayout by bindView(R.id.tab_layout)
    private val appBarLayout: AppBarLayout by bindView(R.id.app_bar)
    private val progressBar: ProgressBar by bindView(R.id.progress_bar)
    private val detailContainer: View by bindView(R.id.detail_container)
    private val backdropImage: ImageView by bindView(R.id.backdrop_image)
    private val collapsingToolbar: CollapsingToolbarLayout by bindView(R.id.collapsing_toolbar)

    private val castViewStub: ViewStub by bindView(R.id.cast_view_stub)
    private val crewViewStub: ViewStub by bindView(R.id.crew_view_stub)
    private val imagesViewStub: ViewStub by bindView(R.id.images_view_stub)
    private val detailContentRecyclerView: RecyclerView by bindView(R.id.detail_content_recycler_view)

    private var menu: Menu? = null
    private var statusBarColor: Int = 0
    private var loadContent: Boolean = true
    private var imagesRecyclerView: RecyclerView? = null
    private var sharedElementEnterTransition: Transition? = null

    protected var imdbId: String? = null
    protected var imageAdapter: ImageAdapter? = null
    protected var castAdapter: RecyclerViewAdapter<Credit>? = null
    protected var crewAdapter: RecyclerViewAdapter<Credit>? = null

    private var reenterState: Bundle? = null

    private val callback = object : SharedElementCallback() {
        override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String, View>?) {
            super.onMapSharedElements(names, sharedElements)
            if (reenterState != null) {
                val currentPosition = reenterState!!.getInt(EXTRA_CURRENT_POSITION)
                val startingPosition = reenterState!!.getInt(EXTRA_STARTING_POSITION)

                if (startingPosition != currentPosition) {
                    val newSharedElement = imagesRecyclerView?.layoutManager?.findViewByPosition(currentPosition)
                    if (newSharedElement != null) {
                        val newTransitionName = "image_$currentPosition"
                        names?.clear()
                        names?.add(newTransitionName)
                        sharedElements?.clear()
                        sharedElements?.put(newTransitionName, newSharedElement)
                    }
                }

                reenterState = null
            } else {
                val statusBar = findViewById(android.R.id.statusBarBackground)
                if (statusBar != null) {
                    names?.add(statusBar.transitionName)
                    sharedElements?.put(statusBar.transitionName, statusBar)
                }

                val navigationBar = findViewById(android.R.id.navigationBarBackground)
                if (navigationBar != null) {
                    names?.add(navigationBar.transitionName)
                    sharedElements?.put(navigationBar.transitionName, navigationBar)
                }
            }
        }
    }

    private val transitionListener = object : TransitionListenerAdapter() {
        override fun onTransitionEnd(transition: Transition) = startLoadingDetailContent()

        override fun onTransitionCancel(transition: Transition) = startLoadingDetailContent()
    }

    private val onImageItemClickListener = object : OnItemClickListener {
        override fun onItemClick(position: Int, view: View) {
            val imageView = view.findViewById(R.id.detail_content_image)
            startImageViewerActivity(imageAdapter?.imageUrlList, "", position, imageView)
        }
    }

    private fun startImageViewerActivity(imageUrlList: ArrayList<String>?, title: String, position: Int, view: View?) {
        if (imageUrlList.isNotNullOrEmpty()) {
            val imagePair = if (view != null) Pair.create(view, "image_$position") else null
            val options = getActivityOptionsCompat(imagePair)

            window.exitTransition = null
            val intent = ImageViewerActivity.createIntent(this, title, position, imageUrlList!!)
            ActivityCompat.startActivity(this, intent, options?.toBundle())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        setExitSharedElementCallback(callback)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        playTrailerFAB?.hide()
        posterImage.setTransitionName(getTransitionNameId())
        showPosterImage(getPosterPath())

        sharedElementEnterTransition = window.sharedElementEnterTransition
        sharedElementEnterTransition?.addListener(transitionListener)

        val regularFont = FontUtils.getTypeface(this, FontUtils.MONTSERRAT_REGULAR)
        collapsingToolbar.setExpandedTitleTypeface(regularFont)
        collapsingToolbar.setCollapsedTitleTypeface(regularFont)
    }

    abstract fun getTransitionNameId(): Int

    private fun startLoadingDetailContent() {
        if (loadContent) {
            loadDetailContent()
            showBackdropImage(getBackdropPath())
            loadContent = false
        }
    }

    abstract fun loadDetailContent(): Unit

    fun showBackdropImage(backdropPath: String) {
        if (backdropPath.isNotEmpty()) {
            backdropImage.loadPaletteBitmap(backdropPath) {
                revealBackdropImage()
                setTopBarColorAndAnimate(it)
            }
        }
    }

    private fun revealBackdropImage() {
        val cx = (backdropImage.left + backdropImage.right) / 2
        val cy = backdropImage.bottom - titleText.height
        val endRadius = Math.max(backdropImage.width, backdropImage.height).toFloat()

        val animator = ViewAnimationUtils.createCircularReveal(backdropImage, cx, cy, 0f, endRadius)
        animator.duration = 400L
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                removeSharedElementTransitionListener()
            }

            override fun onAnimationCancel(animation: Animator?) {
                removeSharedElementTransitionListener()
            }

            override fun onAnimationRepeat(animation: Animator?) {}
        })

        backdropImage.show()
        animator.start()
    }

    private fun removeSharedElementTransitionListener() {
        sharedElementEnterTransition?.removeListener(transitionListener)
    }

    abstract fun getBackdropPath(): String

    private fun setTopBarColorAndAnimate(paletteBitmap: PaletteBitmap?) {
        if (paletteBitmap != null) {
            val palette = paletteBitmap.palette
            val isDark = paletteBitmap.bitmap.isDark(palette)

            if (!isDark) {
                window.decorView.setLightStatusBar()
                val transBlack = getColorCompat(R.color.black_80_transparent)

                val backButton = toolbar?.getChildAt(0) as ImageButton?
                backButton?.setColorFilter(transBlack)

                collapsingToolbar.setCollapsedTitleTextColor(transBlack)
                toolbar?.getOverflowMenuButton()?.setColorFilter(transBlack)
            }

            /**
             * Color the status bar. Set a complementary dark color on L,
             * light or dark color on M (with matching status bar icons)
             */
            statusBarColor = window.statusBarColor
            val rgbColor = palette.getSwatchWithMostPixels()?.rgb

            if (rgbColor != null) {
                statusBarColor = rgbColor.scrimify(isDark)
                collapsingToolbar.setContentScrimColor(rgbColor)

                if (statusBarColor != window.statusBarColor) {
                    animateColorChange(window.statusBarColor, statusBarColor, 500L) { window.statusBarColor = it }
                }
            }
        }
    }

    fun showPosterImage(posterImagePath: String) {
        if (posterImagePath.isNotEmpty()) {
            posterImage.loadPaletteBitmap(posterImagePath) {
                startPostponedEnterTransition()

                it.setPaletteColor { swatch ->
                    titleText.animateBackgroundColorChange(Color.TRANSPARENT, swatch.rgb)
                    titleText.animateTextColorChange(getColorCompat(R.color.primary_text_light), swatch.bodyTextColor)
                }
            }
        }
    }

    abstract fun getPosterPath(): String

    override fun showProgress() = progressBar.show()

    override fun hideProgress() = progressBar.hide()

    override fun showDetailContent(detailContent: I) {
        detailContainer.show()
        showOrHideIMDbMenu()
        changeMenuItemFont()
        appBarLayout.addOnOffsetChangedListener(this)
    }

    override fun showDetailContentList(contentList: List<String>) {
        when (getDetailContentType()) {
            ADAPTER_TYPE_MOVIE -> setDetailContentAdapter(R.array.movie_detail_content_title, contentList)
            ADAPTER_TYPE_TV_SHOW -> setDetailContentAdapter(R.array.tv_detail_content_title, contentList)
            ADAPTER_TYPE_SEASON -> setDetailContentAdapter(R.array.season_detail_content_title, contentList)
            ADAPTER_TYPE_EPISODE -> setDetailContentAdapter(R.array.episode_detail_content_title, contentList)
            ADAPTER_TYPE_PERSON -> setDetailContentAdapter(R.array.person_detail_content_title, contentList)
        }
    }

    abstract fun getDetailContentType(): Int

    private fun setDetailContentAdapter(contentTitleId: Int, contentList: List<String>) {
        detailContentRecyclerView.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(this@BaseDetailActivity)
            adapter = DetailContentAdapter(resources.getStringArray(contentTitleId), contentList)
        }
    }

    override fun showImageList(imageUrlList: ArrayList<String>) {
        imageAdapter = ImageAdapter(imageUrlList, onImageItemClickListener)
        imagesRecyclerView = inflateViewStubRecyclerView(imagesViewStub, R.id.detail_images_recycler_view, imageAdapter!!)
    }

    override fun showTrailerFAB(trailerUrl: String) {
        playTrailerFAB?.show()
        playTrailerFAB?.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOUTUBE_BASE_URL + trailerUrl)))
        }
    }

    override fun showCastList(castList: List<Credit>) {
        castAdapter = RecyclerViewAdapter(R.layout.list_item_content_alt, ADAPTER_TYPE_CREDIT,
                getCastItemClickListener())

        inflateViewStubRecyclerView(castViewStub, R.id.cast_recycler_view, castAdapter!!, castList)
    }

    abstract fun getCastItemClickListener(): OnItemClickListener?

    override fun showCrewList(crewList: List<Credit>) {
        crewAdapter = RecyclerViewAdapter(R.layout.list_item_content_alt, ADAPTER_TYPE_CREDIT,
                getCrewItemClickListener())

        inflateViewStubRecyclerView(crewViewStub, R.id.crew_recycler_view, crewAdapter!!, crewList)
    }

    abstract fun getCrewItemClickListener(): OnItemClickListener?

    protected fun inflateViewStubRecyclerView(viewStub: ViewStub, @IdRes viewId: Int,
                                              adapter: RecyclerView.Adapter<*>): RecyclerView {
        val inflatedView = viewStub.inflate()
        val recyclerView = inflatedView.findViewById(viewId) as RecyclerView
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recyclerView.apply {
            recyclerView.layoutManager = layoutManager
            addItemDecoration(ItemOffsetDecoration())
            recyclerView.adapter = adapter
            val snapHelper = GravitySnapHelper(Gravity.START)
            snapHelper.attachToRecyclerView(this)
        }

        return recyclerView
    }

    protected fun <I : ViewType> inflateViewStubRecyclerView(viewStub: ViewStub, @IdRes viewId: Int,
                                                             adapter: RecyclerViewAdapter<I>,
                                                             itemList: List<I>): RecyclerView {
        val recyclerView = inflateViewStubRecyclerView(viewStub, viewId, adapter)
        adapter.showItemList(itemList)
        return recyclerView
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        playTrailerFAB?.apply {
            val isCollapsing = collapsingToolbar.height + verticalOffset <
                    2.4 * ViewCompat.getMinimumHeight(collapsingToolbar)
            if (isCollapsing) hide() else show()
        }

        if (appBarLayout.totalScrollRange + verticalOffset == 0) {
            collapsingToolbar.title = getItemTitle()
        } else {
            collapsingToolbar.title = ""
        }
    }

    abstract fun getItemTitle(): String

    protected fun startActivityWithTransition(view: View, transitionNameId: Int, intent: Intent) {
        if (Utils.isOnline()) {
            val imagePair = view.getPosterImagePair(transitionNameId)
            val options = getActivityOptionsCompat(imagePair)

            window.exitTransition = null
            ActivityCompat.startActivity(this, intent, options?.toBundle())
        } else {
            showMessage(R.string.error_no_internet)
        }
    }

    override fun finishActivity() = supportFinishAfterTransition()

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)

        reenterState = Bundle(data?.extras)
        val currentPosition = reenterState?.getInt(EXTRA_CURRENT_POSITION)
        val startingPosition = reenterState?.getInt(EXTRA_STARTING_POSITION)
        if (startingPosition != currentPosition) {
            imagesRecyclerView?.smoothScrollToPosition(currentPosition!!)
        }

        postponeEnterTransition()
        imagesRecyclerView?.viewTreeObserver?.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                imagesRecyclerView?.viewTreeObserver?.removeOnPreDrawListener(this)
                startPostponedEnterTransition()
                return true
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        this.menu = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_imdb) {
            if (imdbId.isNotNullOrEmpty()) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(IMDB_BASE_URL + imdbId)))
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showOrHideIMDbMenu() {
        val imdb = menu?.findItem(R.id.action_imdb)
        imdb?.isVisible = imdbId.isNotNullOrEmpty()
    }

    private fun changeMenuItemFont() {
        val typeface = FontUtils.getTypeface(this, FontUtils.MONTSERRAT_REGULAR)
        val customTypefaceSpan = CustomTypefaceSpan(typeface)
        menu?.changeMenuFont(customTypefaceSpan)
    }

    override fun onDestroy() {
        performCleanup()
        super.onDestroy()
    }

    protected open fun performCleanup() {
        castAdapter?.removeListener()
        crewAdapter?.removeListener()
        imageAdapter?.removeListener()
        removeSharedElementTransitionListener()
        imagesRecyclerView?.adapter = null
        appBarLayout.removeOnOffsetChangedListener(this)
    }
}