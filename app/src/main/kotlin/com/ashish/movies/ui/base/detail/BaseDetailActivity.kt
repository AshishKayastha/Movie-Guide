package com.ashish.movies.ui.base.detail

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.IdRes
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.app.SharedElementCallback
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.Transition
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import butterknife.bindOptionalView
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.data.models.Credit
import com.ashish.movies.data.models.OMDbDetail
import com.ashish.movies.ui.base.mvp.MvpActivity
import com.ashish.movies.ui.common.adapter.*
import com.ashish.movies.ui.common.palette.PaletteBitmap
import com.ashish.movies.ui.imageviewer.ImageViewerActivity
import com.ashish.movies.ui.imageviewer.ImageViewerActivity.Companion.EXTRA_CURRENT_POSITION
import com.ashish.movies.ui.imageviewer.ImageViewerActivity.Companion.EXTRA_STARTING_POSITION
import com.ashish.movies.ui.widget.FontTextView
import com.ashish.movies.ui.widget.ItemOffsetDecoration
import com.ashish.movies.utils.*
import com.ashish.movies.utils.Constants.ADAPTER_TYPE_CREDIT
import com.ashish.movies.utils.Constants.ADAPTER_TYPE_EPISODE
import com.ashish.movies.utils.Constants.ADAPTER_TYPE_MOVIE
import com.ashish.movies.utils.Constants.ADAPTER_TYPE_PERSON
import com.ashish.movies.utils.Constants.ADAPTER_TYPE_SEASON
import com.ashish.movies.utils.Constants.ADAPTER_TYPE_TV_SHOW
import com.ashish.movies.utils.Constants.IMDB_BASE_URL
import com.ashish.movies.utils.Constants.YOUTUBE_BASE_URL
import com.ashish.movies.utils.extensions.*
import java.util.*

/**
 * Created by Ashish on Jan 03.
 */
abstract class BaseDetailActivity<I, V : BaseDetailView<I>, P : BaseDetailPresenter<I, V>>
    : MvpActivity<V, P>(), BaseDetailView<I>, AppBarLayout.OnOffsetChangedListener {

    protected var menu: Menu? = null
    protected var imdbId: String? = null
    protected var castAdapter: RecyclerViewAdapter<Credit>? = null
    protected var crewAdapter: RecyclerViewAdapter<Credit>? = null

    protected val titleText: FontTextView by bindView(R.id.content_title_text)

    private val tabLayout: TabLayout by bindView(R.id.tab_layout)
    private val appBarLayout: AppBarLayout by bindView(R.id.app_bar)
    private val progressBar: ProgressBar by bindView(R.id.progress_bar)
    private val detailContainer: View by bindView(R.id.detail_container)
    private val backdropImage: ImageView by bindView(R.id.backdrop_image)
    private val posterImage: ImageView by bindView(R.id.detail_poster_image)
    private val collapsingToolbar: CollapsingToolbarLayout by bindView(R.id.collapsing_toolbar)
    private val playTrailerFAB: FloatingActionButton? by bindOptionalView(R.id.play_trailer_fab)

    private val castViewStub: ViewStub by bindView(R.id.cast_view_stub)
    private val crewViewStub: ViewStub by bindView(R.id.crew_view_stub)
    private val imagesViewStub: ViewStub by bindView(R.id.images_view_stub)
    private val detailContentRecyclerView: RecyclerView by bindView(R.id.detail_content_recycler_view)

    private var statusBarColor: Int = 0
    private var loadContent: Boolean = true
    private lateinit var regularFont: Typeface
    private var rottenTomatoesUrl: String? = null

    private var reenterState: Bundle? = null
    private var imageAdapter: ImageAdapter? = null
    private var imagesRecyclerView: RecyclerView? = null
    private var sharedElementEnterTransition: Transition? = null

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
                        sharedElements?.clear()
                        names?.add(newTransitionName)
                        sharedElements?.put(newTransitionName, newSharedElement)
                    }
                }

                reenterState = null
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
            val intent = ImageViewerActivity.createIntent(this, title, position, imageUrlList!!)
            startActivityWithTransition(imagePair, intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        setExitSharedElementCallback(callback)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        posterImage.setTransitionName(getTransitionNameId())
        showPosterImage(getPosterPath())

        sharedElementEnterTransition = window.sharedElementEnterTransition
        sharedElementEnterTransition?.addListener(transitionListener)

        regularFont = FontUtils.getTypeface(this, FontUtils.MONTSERRAT_REGULAR)
        collapsingToolbar.apply {
            setExpandedTitleTypeface(regularFont)
            setCollapsedTitleTypeface(regularFont)
        }
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

    protected fun showBackdropImage(backdropPath: String) {
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

        backdropImage.startCircularRevealAnimation(cx, cy, 0f, endRadius) {
            removeSharedElementTransitionListener()
        }
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
                val primaryBlack = getColorCompat(R.color.primary_text_dark)

                val backButton = toolbar?.getChildAt(0) as ImageButton?
                backButton?.setColorFilter(primaryBlack)

                collapsingToolbar.setCollapsedTitleTextColor(primaryBlack)
                toolbar?.getOverflowMenuButton()?.setColorFilter(primaryBlack)
            }

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

    private fun showPosterImage(posterImagePath: String) {
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
        showOrHideMenu(R.id.action_imdb, imdbId)
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
        imagesRecyclerView = inflateViewStubRecyclerView(imagesViewStub, R.id.detail_images_recycler_view,
                imageAdapter!!)
    }

    override fun showTrailerFAB(trailerUrl: String) {
        playTrailerFAB?.apply {
            postDelayed({ animate().alpha(1f).scaleX(1f).scaleY(1f).start() }, 80L)
            setOnClickListener { openUrl(YOUTUBE_BASE_URL + trailerUrl) }
        }
    }

    @CallSuper
    override fun showOMDbDetail(omDbDetail: OMDbDetail) {
        rottenTomatoesUrl = omDbDetail.tomatoURL
        showOrHideMenu(R.id.action_rotten_tomatoes, rottenTomatoesUrl)
        menu?.changeMenuFont(CustomTypefaceSpan(regularFont))
    }

    protected fun showOrHideMenu(menuItemId: Int, text: String?) {
        val menuItem = menu?.findItem(menuItemId)
        menuItem?.isVisible = text.isNotNullOrEmpty()
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

    private fun inflateViewStubRecyclerView(viewStub: ViewStub, @IdRes viewId: Int,
                                            adapter: RecyclerView.Adapter<*>): RecyclerView {
        val inflatedView = viewStub.inflate()
        val recyclerView = inflatedView.find<RecyclerView>(viewId)

        recyclerView.apply {
            setHasFixedSize(true)
            addItemDecoration(ItemOffsetDecoration())
            layoutManager = LinearLayoutManager(this@BaseDetailActivity, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = adapter
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
            setVisibility(!isCollapsing)
        }

        if (appBarLayout.totalScrollRange + verticalOffset == 0) {
            collapsingToolbar.title = getItemTitle()
        } else {
            collapsingToolbar.title = ""
        }
    }

    abstract fun getItemTitle(): String

    protected fun startNewActivityWithTransition(view: View, transitionNameId: Int, intent: Intent) {
        if (Utils.isOnline()) {
            val imagePair = view.getPosterImagePair(transitionNameId)
            startActivityWithTransition(imagePair, intent)
        } else {
            showMessage(R.string.error_no_internet)
        }
    }

    override fun finishActivity() = finishAfterTransition()

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)

        if (data != null) {
            reenterState = data.extras
            val currentPosition = reenterState?.getInt(EXTRA_CURRENT_POSITION)
            val startingPosition = reenterState?.getInt(EXTRA_STARTING_POSITION)

            if (startingPosition != currentPosition) {
                imagesRecyclerView?.smoothScrollToPosition(currentPosition!!)
            }

            postponeEnterTransition()
            imagesRecyclerView?.viewTreeObserver?.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    imagesRecyclerView?.viewTreeObserver?.removeOnPreDrawListener(this)
                    imagesRecyclerView?.requestLayout()
                    startPostponedEnterTransition()
                    return true
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        this.menu = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_imdb -> {
                if (imdbId.isNotNullOrEmpty()) {
                    openUrl(IMDB_BASE_URL + imdbId)
                }
                return true
            }

            R.id.action_rotten_tomatoes -> {
                if (rottenTomatoesUrl.isNotNullOrEmpty()) {
                    openUrl(rottenTomatoesUrl!!)
                }
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun finishAfterTransition() {
        playTrailerFAB?.hide()
        super.finishAfterTransition()
    }

    override fun onDestroy() {
        performCleanup()
        super.onDestroy()
    }

    protected open fun performCleanup() {
        castAdapter?.removeListener()
        crewAdapter?.removeListener()
        imageAdapter?.removeListener()
        imagesRecyclerView?.adapter = null
        removeSharedElementTransitionListener()
        appBarLayout.removeOnOffsetChangedListener(this)
    }
}