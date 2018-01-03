package com.ashish.movieguide.ui.base.detail

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.IdRes
import android.support.design.widget.AppBarLayout
import android.support.v4.app.ShareCompat
import android.support.v4.util.Pair
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.Transition
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewStub
import android.view.ViewTreeObserver
import android.widget.ImageButton
import android.widget.ImageView
import com.ashish.movieguide.R
import com.ashish.movieguide.data.network.entities.common.OMDbDetail
import com.ashish.movieguide.data.network.entities.tmdb.Credit
import com.ashish.movieguide.ui.base.mvp.MvpActivity
import com.ashish.movieguide.ui.common.adapter.DetailContentAdapter
import com.ashish.movieguide.ui.common.adapter.ImageAdapter
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.ui.common.adapter.RecyclerViewAdapter
import com.ashish.movieguide.ui.common.adapter.ViewType
import com.ashish.movieguide.ui.imageviewer.ImageViewerActivity
import com.ashish.movieguide.ui.imageviewer.ImageViewerActivity.Companion.EXTRA_CURRENT_POSITION
import com.ashish.movieguide.ui.imageviewer.ImageViewerActivity.Companion.EXTRA_STARTING_POSITION
import com.ashish.movieguide.ui.widget.ItemOffsetDecoration
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_CREDIT
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_EPISODE
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_MOVIE
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_PERSON
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_SEASON
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_TV_SHOW
import com.ashish.movieguide.utils.Constants.IMDB_BASE_URL
import com.ashish.movieguide.utils.Constants.LIST_THUMBNAIL_HEIGHT
import com.ashish.movieguide.utils.Constants.LIST_THUMBNAIL_WIDTH
import com.ashish.movieguide.utils.CustomTypefaceSpan
import com.ashish.movieguide.utils.FontUtils
import com.ashish.movieguide.utils.StartSnapHelper
import com.ashish.movieguide.utils.TransitionListenerAdapter
import com.ashish.movieguide.utils.extensions.animateBackgroundColorChange
import com.ashish.movieguide.utils.extensions.animateTextColorChange
import com.ashish.movieguide.utils.extensions.changeMenuAndSubMenuFont
import com.ashish.movieguide.utils.extensions.changeTitleTypeface
import com.ashish.movieguide.utils.extensions.find
import com.ashish.movieguide.utils.extensions.get
import com.ashish.movieguide.utils.extensions.getColorCompat
import com.ashish.movieguide.utils.extensions.getPosterImagePair
import com.ashish.movieguide.utils.extensions.getStringArray
import com.ashish.movieguide.utils.extensions.hide
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.extensions.loadPaletteBitmap
import com.ashish.movieguide.utils.extensions.openUrl
import com.ashish.movieguide.utils.extensions.setOverflowMenuColor
import com.ashish.movieguide.utils.extensions.setPaletteColor
import com.ashish.movieguide.utils.extensions.setTopBarColorAndAnimate
import com.ashish.movieguide.utils.extensions.setTransitionName
import com.ashish.movieguide.utils.extensions.show
import com.ashish.movieguide.utils.extensions.startActivityWithTransition
import com.ashish.movieguide.utils.extensions.startCircularRevealAnimation
import com.ashish.movieguide.utils.extensions.tint
import com.ashish.movieguide.utils.transition.LeakFreeSupportSharedElementCallback
import kotlinx.android.synthetic.main.layout_detail_app_bar.*
import kotlinx.android.synthetic.main.layout_detail_cast_credits_stub.*
import kotlinx.android.synthetic.main.layout_detail_content_recycler_view.*
import kotlinx.android.synthetic.main.layout_detail_crew_credits_stub.*
import kotlinx.android.synthetic.main.layout_detail_images_stub.*
import kotlinx.android.synthetic.main.layout_detail_progress_bar.*
import java.util.ArrayList

/**
 * This is a base class which handles common logic for showing
 * detail contents provided by TMDb and Trakt api.
 * @param I TMDB Item
 * @param T Trakt Item
 */
abstract class BaseDetailActivity<I, T, V : BaseDetailView<I>, P : BaseDetailPresenter<I, T, V>>
    : MvpActivity<V, P>(), BaseDetailView<I>, AppBarLayout.OnOffsetChangedListener {

    protected var menu: Menu? = null
    protected var imdbId: String? = null
    protected var castAdapter: RecyclerViewAdapter<Credit>? = null
    protected var crewAdapter: RecyclerViewAdapter<Credit>? = null

    private var loadContent: Boolean = true
    private lateinit var regularFont: Typeface
    private var rottenTomatoesUrl: String? = null

    private var reenterState: Bundle? = null
    private var imageAdapter: ImageAdapter? = null
    private var imagesRecyclerView: RecyclerView? = null
    private var sharedElementEnterTransition: Transition? = null

    private val callback = object : LeakFreeSupportSharedElementCallback() {
        override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String, View>?) {
            super.onMapSharedElements(names, sharedElements)

            if (reenterState != null) {
                val currentPosition = reenterState!!.getInt(EXTRA_CURRENT_POSITION)
                val startingPosition = reenterState!!.getInt(EXTRA_STARTING_POSITION)

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

    private val transitionListener = object : TransitionListenerAdapter() {
        override fun onTransitionEnd(transition: Transition) = startLoadingDetailContent()

        override fun onTransitionCancel(transition: Transition) = startLoadingDetailContent()
    }

    private val onImageItemClickListener = object : OnItemClickListener {
        override fun onItemClick(position: Int, view: View) {
            val imageView = view.findViewById(R.id.detail_content_image) as ImageView
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

        /*
          Postpone shared element transition in onCreate
          as the poster image is not loaded fully here.
          It will be resumed when the poster image is loaded.
         */
        postponeEnterTransition()
        setExitSharedElementCallback(callback)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        detailPosterImage.setTransitionName(getTransitionNameId())
        showPosterImage(getPosterPath())

        sharedElementEnterTransition = window.sharedElementEnterTransition
        sharedElementEnterTransition?.addListener(transitionListener)

        collapsingToolbar.changeTitleTypeface()
        regularFont = FontUtils.getTypeface(this, FontUtils.MONTSERRAT_REGULAR)
    }

    abstract fun getTransitionNameId(): Int

    private fun startLoadingDetailContent() {
        if (loadContent) {
            loadDetailContent()
            showBackdropImage(getBackdropPath())
            loadContent = false
        }
    }

    abstract fun loadDetailContent()

    protected fun showBackdropImage(backdropPath: String) {
        backdropImage.loadPaletteBitmap(backdropPath) { paletteBitmap ->
            revealBackdropImage()
            setTopBarColorAndAnimate(paletteBitmap, collapsingToolbar, this::tintTopBarIconsToBlack)
        }
    }

    private fun revealBackdropImage() {
        /*
          Circular Reveal animation values:
          x coordinate - center of backdrop image
          y coordinate - top of title text view
          start radius - 0
          end radius - maximum value between backdrop image width and height
         */
        val cx = (backdropImage.left + backdropImage.right) / 2
        val cy = backdropImage.bottom - contentTitleText.height
        val endRadius = Math.max(backdropImage.width, backdropImage.height).toFloat()

        backdropImage.startCircularRevealAnimation(cx = cx, cy = cy, startRadius = 0f, endRadius = endRadius,
                animationEnd = this::removeSharedElementTransitionListener)
    }

    private fun removeSharedElementTransitionListener() {
        sharedElementEnterTransition?.removeListener(transitionListener)
    }

    abstract fun getBackdropPath(): String

    private fun tintTopBarIconsToBlack() {
        val primaryBlack = getColorCompat(R.color.primary_text_dark)

        val backButton = toolbar[0] as ImageButton?
        backButton?.setColorFilter(primaryBlack)

        menu?.tint(primaryBlack)
        setOverflowMenuColor(primaryBlack)
        collapsingToolbar.setCollapsedTitleTextColor(primaryBlack)
    }

    private fun showPosterImage(posterImagePath: String) {
        detailPosterImage.loadPaletteBitmap(posterImagePath, LIST_THUMBNAIL_WIDTH,
                LIST_THUMBNAIL_HEIGHT) { paletteBitmap ->
            // When the poster image is loaded then
            // start postponed shared element transition
            startPostponedEnterTransition()

            paletteBitmap.setPaletteColor { swatch ->
                contentTitleText.animateBackgroundColorChange(Color.TRANSPARENT, swatch.rgb)
                contentTitleText.animateTextColorChange(getColorCompat(R.color.primary_text_light),
                        swatch.bodyTextColor)
            }
        }
    }

    abstract fun getPosterPath(): String

    override fun showProgress() = progressBar.show()

    override fun hideProgress() = progressBar.hide()

    override fun showDetailContent(detailContent: I) {
        appBar.addOnOffsetChangedListener(this)
        showOrHideMenu(R.id.action_imdb, imdbId)
        menu?.findItem(R.id.action_share)?.isVisible = true
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
        detailContentRecyclerView.run {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(this@BaseDetailActivity)
            adapter = DetailContentAdapter(getStringArray(contentTitleId), contentList)
        }
    }

    override fun showImageList(imageUrlList: ArrayList<String>) {
        imageAdapter = ImageAdapter(imageUrlList, onImageItemClickListener)
        imagesRecyclerView = inflateViewStubRecyclerView(imagesViewStub, R.id.detailImagesRecyclerView,
                imageAdapter!!)
    }

    @CallSuper
    override fun showOMDbDetail(omDbDetail: OMDbDetail) {
        rottenTomatoesUrl = omDbDetail.tomatoURL
        showOrHideMenu(R.id.action_rotten_tomatoes, rottenTomatoesUrl)
        menu?.changeMenuAndSubMenuFont(CustomTypefaceSpan(regularFont))
    }

    private fun showOrHideMenu(menuItemId: Int, text: String?) {
        val menuItem = menu?.findItem(menuItemId)
        menuItem?.isVisible = text.isNotNullOrEmpty()
    }

    override fun showCastList(castList: List<Credit>) {
        castAdapter = RecyclerViewAdapter(R.layout.list_item_content_alt, ADAPTER_TYPE_CREDIT,
                getCastItemClickListener())

        inflateViewStubRecyclerView(castViewStub, R.id.castRecyclerView, castAdapter!!, castList)
    }

    abstract fun getCastItemClickListener(): OnItemClickListener?

    override fun showCrewList(crewList: List<Credit>) {
        crewAdapter = RecyclerViewAdapter(R.layout.list_item_content_alt, ADAPTER_TYPE_CREDIT,
                getCrewItemClickListener())

        inflateViewStubRecyclerView(crewViewStub, R.id.crewRecyclerView, crewAdapter!!, crewList)
    }

    abstract fun getCrewItemClickListener(): OnItemClickListener?

    protected fun inflateViewStubRecyclerView(viewStub: ViewStub, @IdRes viewId: Int,
                                              adapter: RecyclerView.Adapter<*>): RecyclerView {
        val inflatedView = viewStub.inflate()
        val recyclerView = inflatedView.find<RecyclerView>(viewId)

        recyclerView.run {
            setHasFixedSize(true)
            addItemDecoration(ItemOffsetDecoration())
            layoutManager = LinearLayoutManager(this@BaseDetailActivity, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = adapter
            val snapHelper = StartSnapHelper()
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

    @CallSuper
    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        if (appBarLayout.totalScrollRange + verticalOffset == 0) {
            collapsingToolbar.title = getItemTitle()
        } else {
            collapsingToolbar.title = ""
        }
    }

    abstract fun getItemTitle(): String

    protected fun startNewActivityWithTransition(view: View, transitionNameId: Int, intent: Intent) {
        val imagePair = view.getPosterImagePair(transitionNameId)
        startActivityWithTransition(imagePair, intent)
    }

    override fun finishActivity() = finishAfterTransition()

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)

        if (data != null) {
            // Get extras passed to this activity from ImageViewerActivity
            reenterState = data.extras
            val currentPosition = reenterState?.getInt(EXTRA_CURRENT_POSITION)
            val startingPosition = reenterState?.getInt(EXTRA_STARTING_POSITION)

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
            postponeEnterTransition()
            imagesRecyclerView?.viewTreeObserver?.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    imagesRecyclerView?.viewTreeObserver?.removeOnPreDrawListener(this)

                    // Fix required for smooth transition
                    imagesRecyclerView?.requestLayout()

                    /*
                      Start postponed shared element transiton when we know
                      that recyclerview is now drawn.
                     */
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

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_share -> shareContent()
        R.id.action_imdb -> viewInIMDbSite()
        R.id.action_rotten_tomatoes -> performAction { openUrl(rottenTomatoesUrl) }
        else -> super.onOptionsItemSelected(item)
    }

    private fun shareContent(): Boolean {
        return performAction {
            val shareIntent = ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setText(getShareText())
                    .intent

            if (shareIntent.resolveActivity(packageManager) != null) {
                startActivity(shareIntent)
            }
        }
    }

    abstract fun getShareText(): CharSequence

    private fun viewInIMDbSite(): Boolean {
        return performAction {
            if (imdbId.isNotNullOrEmpty()) {
                openUrl(IMDB_BASE_URL + imdbId)
            }
        }
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
        appBar.removeOnOffsetChangedListener(this)
    }
}