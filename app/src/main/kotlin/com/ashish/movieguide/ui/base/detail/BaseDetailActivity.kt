package com.ashish.movieguide.ui.base.detail

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.design.widget.AppBarLayout
import android.support.v4.app.ShareCompat
import android.support.v7.widget.LinearLayoutManager
import android.transition.Transition
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.ashish.movieguide.R
import com.ashish.movieguide.data.network.entities.common.OMDbDetail
import com.ashish.movieguide.data.network.entities.tmdb.Credit
import com.ashish.movieguide.ui.base.mvp.MvpActivity
import com.ashish.movieguide.ui.common.adapter.DetailContentAdapter
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.ui.common.adapter.RecyclerViewAdapter
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
import com.ashish.movieguide.utils.TransitionListenerAdapter
import com.ashish.movieguide.utils.extensions.animateBackgroundColorChange
import com.ashish.movieguide.utils.extensions.animateTextColorChange
import com.ashish.movieguide.utils.extensions.changeMenuAndSubMenuFont
import com.ashish.movieguide.utils.extensions.changeTitleTypeface
import com.ashish.movieguide.utils.extensions.getColorCompat
import com.ashish.movieguide.utils.extensions.getPosterImagePair
import com.ashish.movieguide.utils.extensions.getStringArray
import com.ashish.movieguide.utils.extensions.inflateToRecyclerView
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.extensions.loadPaletteBitmap
import com.ashish.movieguide.utils.extensions.openUrl
import com.ashish.movieguide.utils.extensions.performAction
import com.ashish.movieguide.utils.extensions.setPaletteColor
import com.ashish.movieguide.utils.extensions.setTopBarColorAndAnimate
import com.ashish.movieguide.utils.extensions.setTransitionName
import com.ashish.movieguide.utils.extensions.setVisibility
import com.ashish.movieguide.utils.extensions.startActivityWithTransition
import com.ashish.movieguide.utils.extensions.startCircularRevealAnimation
import kotlinx.android.synthetic.main.layout_detail_app_bar.*
import kotlinx.android.synthetic.main.layout_detail_cast_credits_stub.*
import kotlinx.android.synthetic.main.layout_detail_content_recycler_view.*
import kotlinx.android.synthetic.main.layout_detail_crew_credits_stub.*
import kotlinx.android.synthetic.main.layout_detail_images_stub.*
import kotlinx.android.synthetic.main.layout_detail_progress_bar.*
import java.util.ArrayList
import javax.inject.Inject

/**
 * This is a base class which handles common logic for showing
 * detail contents provided by TMDb and Trakt api.
 * @param I TMDB Item
 * @param T Trakt Item
 */
abstract class BaseDetailActivity<I, T, V : BaseDetailView<I>, P : BaseDetailPresenter<I, T, V>>
    : MvpActivity<V, P>(), BaseDetailView<I>, AppBarLayout.OnOffsetChangedListener {

    @Inject lateinit var detailImageManager: DetailImageManager

    protected var menu: Menu? = null
    protected var imdbId: String? = null
    protected var castAdapter: RecyclerViewAdapter<Credit>? = null
    protected var crewAdapter: RecyclerViewAdapter<Credit>? = null

    private var rottenTomatoesUrl: String? = null
    private var sharedElementEnterTransition: Transition? = null

    private val transitionListener = object : TransitionListenerAdapter() {
        override fun onTransitionEnd(transition: Transition) {
            loadDetailContent()
            showBackdropImage(getBackdropPath())
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
        detailImageManager.initImageTransition()

        collapsingToolbar.changeTitleTypeface()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        detailPosterImage.setTransitionName(getTransitionNameId())
        showPosterImage(getPosterPath())

        sharedElementEnterTransition = window.sharedElementEnterTransition
        sharedElementEnterTransition?.addListener(transitionListener)
    }

    abstract fun getTransitionNameId(): Int

    abstract fun getPosterPath(): String

    private fun showPosterImage(posterImagePath: String) {
        detailPosterImage.loadPaletteBitmap(posterImagePath, LIST_THUMBNAIL_WIDTH,
                LIST_THUMBNAIL_HEIGHT) { paletteBitmap ->
            // When the poster image is loaded then
            // start postponed shared element transition
            startPostponedEnterTransition()

            paletteBitmap?.palette.setPaletteColor { swatch ->
                contentTitleText.apply {
                    animateBackgroundColorChange(Color.TRANSPARENT, swatch.rgb)
                    animateTextColorChange(getColorCompat(R.color.primary_text_light), swatch.bodyTextColor)
                }
            }
        }
    }

    abstract fun loadDetailContent()

    abstract fun getBackdropPath(): String

    protected fun showBackdropImage(backdropPath: String) {
        backdropImage.loadPaletteBitmap(backdropPath) { paletteBitmap ->
            revealBackdropImage()
            setTopBarColorAndAnimate(paletteBitmap, collapsingToolbar, menu, toolbar)
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
        backdropImage.startCircularRevealAnimation(
                cx = (backdropImage.left + backdropImage.right) / 2,
                cy = backdropImage.bottom - contentTitleText.height,
                startRadius = 0f,
                endRadius = Math.max(backdropImage.width, backdropImage.height).toFloat(),
                animationEnd = ::removeSharedElementTransitionListener
        )
    }

    private fun removeSharedElementTransitionListener() {
        sharedElementEnterTransition?.removeListener(transitionListener)
    }

    override fun setLoadingIndicator(showIndicator: Boolean) {
        progressBar.setVisibility(showIndicator)
    }

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

    private fun setDetailContentAdapter(contentTitleArrayId: Int, contentList: List<String>) {
        detailContentRecyclerView.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(this@BaseDetailActivity)
            adapter = DetailContentAdapter(getStringArray(contentTitleArrayId), contentList)
        }
    }

    override fun showImageList(imageUrlList: ArrayList<String>) {
        detailImageManager.showImageList(imagesViewStub, imageUrlList)
    }

    @CallSuper
    override fun showOMDbDetail(omDbDetail: OMDbDetail) {
        rottenTomatoesUrl = omDbDetail.tomatoURL
        showOrHideMenu(R.id.action_rotten_tomatoes, rottenTomatoesUrl)
        val regularFont = FontUtils.getTypeface(this)
        menu?.changeMenuAndSubMenuFont(CustomTypefaceSpan(regularFont))
    }

    private fun showOrHideMenu(menuItemId: Int, text: String?) {
        menu?.findItem(menuItemId)?.isVisible = text.isNotNullOrEmpty()
    }

    override fun showCastList(castList: List<Credit>) {
        castAdapter = RecyclerViewAdapter(
                R.layout.list_item_content_alt,
                ADAPTER_TYPE_CREDIT,
                getCastItemClickListener()
        )

        castViewStub.inflateToRecyclerView(this, R.id.castRecyclerView, castAdapter!!, castList)
    }

    abstract fun getCastItemClickListener(): OnItemClickListener?

    override fun showCrewList(crewList: List<Credit>) {
        crewAdapter = RecyclerViewAdapter(
                R.layout.list_item_content_alt,
                ADAPTER_TYPE_CREDIT,
                getCrewItemClickListener()
        )

        crewViewStub.inflateToRecyclerView(this, R.id.crewRecyclerView, crewAdapter!!, crewList)
    }

    abstract fun getCrewItemClickListener(): OnItemClickListener?

    @CallSuper
    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        collapsingToolbar.title = if (appBarLayout.totalScrollRange + verticalOffset == 0) {
            getItemTitle()
        } else ""
    }

    abstract fun getItemTitle(): String

    protected fun startNewActivityWithTransition(view: View, transitionNameId: Int, intent: Intent) {
        val imagePair = view.getPosterImagePair(transitionNameId)
        startActivityWithTransition(imagePair, intent)
    }

    override fun finishActivity() = finishAfterTransition()

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        data?.let { detailImageManager.fixImagePositionOnActivityReenter(it) }
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

    private fun shareContent(): Boolean = performAction {
        val shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(getShareText())
                .intent

        if (shareIntent.resolveActivity(packageManager) != null) {
            startActivity(shareIntent)
        }
    }

    abstract fun getShareText(): CharSequence

    private fun viewInIMDbSite(): Boolean = performAction {
        if (imdbId.isNotNullOrEmpty()) {
            openUrl(IMDB_BASE_URL + imdbId)
        }
    }

    override fun onDestroy() {
        performCleanup()
        super.onDestroy()
    }

    protected open fun performCleanup() {
        castAdapter?.removeListener()
        crewAdapter?.removeListener()
        detailImageManager.performCleanup()
        removeSharedElementTransitionListener()
        appBar.removeOnOffsetChangedListener(this)
    }
}