package com.ashish.movies.ui.base.detail

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.Transition
import android.view.View
import android.view.ViewStub
import android.widget.ImageButton
import android.widget.ImageView
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.data.models.Credit
import com.ashish.movies.ui.base.mvp.MvpActivity
import com.ashish.movies.ui.common.adapter.RecyclerViewAdapter
import com.ashish.movies.ui.common.adapter.RecyclerViewAdapter.Companion.ADAPTER_TYPE_CREDIT
import com.ashish.movies.ui.common.adapter.ViewType
import com.ashish.movies.ui.common.palette.PaletteBitmap
import com.ashish.movies.ui.widget.FontTextView
import com.ashish.movies.ui.widget.ItemOffsetDecoration
import com.ashish.movies.utils.FontUtils
import com.ashish.movies.utils.extensions.animateBackgroundColorChange
import com.ashish.movies.utils.extensions.animateColorChange
import com.ashish.movies.utils.extensions.animateTextColorChange
import com.ashish.movies.utils.extensions.dpToPx
import com.ashish.movies.utils.extensions.getActivityOptionsCompat
import com.ashish.movies.utils.extensions.getColorCompat
import com.ashish.movies.utils.extensions.getPosterImagePair
import com.ashish.movies.utils.extensions.hide
import com.ashish.movies.utils.extensions.isDark
import com.ashish.movies.utils.extensions.loadPaletteBitmap
import com.ashish.movies.utils.extensions.scrimify
import com.ashish.movies.utils.extensions.setLightStatusBar
import com.ashish.movies.utils.extensions.setPaletteColor
import com.ashish.movies.utils.extensions.show
import me.zhanghai.android.materialprogressbar.MaterialProgressBar

/**
 * Created by Ashish on Jan 03.
 */
abstract class BaseDetailActivity<I : ViewType, V : BaseDetailMvpView<I>, P : BaseDetailPresenter<I, V>>
    : MvpActivity<V, P>(), BaseDetailMvpView<I>, AppBarLayout.OnOffsetChangedListener {

    protected val appBarLayout: AppBarLayout by bindView(R.id.app_bar)
    protected val backdropImage: ImageView by bindView(R.id.backdrop_image)
    protected val posterImage: ImageView by bindView(R.id.poster_image)
    protected val detailContainer: View by bindView(R.id.detail_container)
    protected val progressBar: MaterialProgressBar by bindView(R.id.material_progress_bar)
    protected val collapsingToolbar: CollapsingToolbarLayout by bindView(R.id.collapsing_toolbar)

    protected val statusText: FontTextView by bindView(R.id.status_text)
    protected val budgetText: FontTextView by bindView(R.id.budget_text)
    protected val genresText: FontTextView by bindView(R.id.genres_text)
    protected val runtimeText: FontTextView by bindView(R.id.runtime_text)
    protected val revenueText: FontTextView by bindView(R.id.revenue_text)
    protected val overviewText: FontTextView by bindView(R.id.overview_text)
    protected val titleText: FontTextView by bindView(R.id.content_title_text)
    protected val releaseDateText: FontTextView by bindView(R.id.release_date_text)

    protected val castViewStub: ViewStub by bindView(R.id.cast_view_stub)
    protected val crewViewStub: ViewStub by bindView(R.id.crew_view_stub)

    protected var item: I? = null

    private var statusBarColor: Int = 0
    private var loadContent: Boolean = true
    private lateinit var sharedElementEnterTransition: Transition

    private lateinit var castAdapter: RecyclerViewAdapter<Credit>
    private lateinit var crewAdapter: RecyclerViewAdapter<Credit>

    protected val transitionListener = object : Transition.TransitionListener {
        override fun onTransitionStart(transition: Transition) {}

        override fun onTransitionEnd(transition: Transition) {
            if (loadContent) {
                loadDetailContent()
                loadContent = false
            }
        }

        override fun onTransitionCancel(transition: Transition) {}

        override fun onTransitionPause(transition: Transition) {}

        override fun onTransitionResume(transition: Transition) {}
    }

    companion object {
        @JvmStatic val ITEM_SPACING = 8f.dpToPx().toInt()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportPostponeEnterTransition()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            getIntentExtras(intent.extras)
        }

        showPosterImage()
        showBackdropImage()
        appBarLayout.addOnOffsetChangedListener(this)

        sharedElementEnterTransition = window.sharedElementEnterTransition
        sharedElementEnterTransition.addListener(transitionListener)

        val regularFont = FontUtils.getTypeface(this, FontUtils.MONTSERRAT_REGULAR)
        collapsingToolbar.setExpandedTitleTypeface(regularFont)
        collapsingToolbar.setCollapsedTitleTypeface(regularFont)
    }

    abstract fun getIntentExtras(extras: Bundle?)

    abstract fun loadDetailContent(): Unit

    fun showBackdropImage() {
        val backdropPath = getBackdropPath()
        if (backdropPath.isNotEmpty()) backdropImage.loadPaletteBitmap(backdropPath)
    }

    abstract fun getBackdropPath(): String

    fun showPosterImage() {
        val posterPath = getPosterPath()
        if (posterPath.isNotEmpty()) {
            posterImage.loadPaletteBitmap(posterPath) { paletteBitmap ->
                supportStartPostponedEnterTransition()

                paletteBitmap.setPaletteColor { swatch ->
                    val rgbColor = swatch.rgb
                    setTopBarColorAndAnimate(paletteBitmap, rgbColor)
                    titleText.animateBackgroundColorChange(Color.TRANSPARENT, rgbColor)
                    titleText.animateTextColorChange(getColorCompat(R.color.primary_text_light), swatch.bodyTextColor)
                }
            }
        }
    }

    private fun setTopBarColorAndAnimate(paletteBitmap: PaletteBitmap?, rgbColor: Int) {
        if (paletteBitmap != null) {
            val isDark = paletteBitmap.bitmap.isDark(paletteBitmap.palette)

            if (!isDark) {
                window.decorView.setLightStatusBar()
                val backButton = toolbar?.getChildAt(0) as ImageButton

                val transBlack = getColorCompat(R.color.black_80_transparent)
                backButton.setColorFilter(transBlack)
                collapsingToolbar.setCollapsedTitleTextColor(transBlack)
            }

            /**
             * Color the status bar. Set a complementary dark color on L,
             * light or dark color on M (with matching status bar icons)
             */
            statusBarColor = window.statusBarColor
            collapsingToolbar.setContentScrimColor(rgbColor)

            if (isDark || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                statusBarColor = rgbColor.scrimify(isDark)
            }

            if (statusBarColor != window.statusBarColor) {
                animateColorChange(window.statusBarColor, statusBarColor, 500L) { window.statusBarColor = it }
            }
        }
    }

    abstract fun getPosterPath(): String

    override fun showDetailContent(item: I?) = detailContainer.show()

    override fun showProgress() = progressBar.show()

    override fun hideProgress() = progressBar.hide()

    override fun showCastList(castList: List<Credit>) {
        castAdapter = RecyclerViewAdapter(R.layout.list_item_content_alt, ADAPTER_TYPE_CREDIT, null)
        inflateViewStubRecyclerView(castViewStub, R.id.cast_recycler_view, castAdapter, castList)
    }

    override fun showCrewList(crewList: List<Credit>) {
        crewAdapter = RecyclerViewAdapter(R.layout.list_item_content_alt, ADAPTER_TYPE_CREDIT, null)
        inflateViewStubRecyclerView(crewViewStub, R.id.crew_recycler_view, crewAdapter, crewList)
    }

    protected fun <I : ViewType> inflateViewStubRecyclerView(viewStub: ViewStub, @IdRes viewId: Int,
                                                             adapter: RecyclerViewAdapter<I>, itemList: List<I>) {
        val inflatedView = viewStub.inflate()
        val recyclerView = inflatedView.findViewById(viewId) as RecyclerView
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recyclerView.apply {
            recyclerView.layoutManager = layoutManager
            addItemDecoration(ItemOffsetDecoration(ITEM_SPACING))
            recyclerView.adapter = adapter
        }

        adapter.showItemList(itemList)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        if (appBarLayout.totalScrollRange + verticalOffset == 0) {
            collapsingToolbar.title = getItemTitle()
        } else {
            collapsingToolbar.title = ""
        }
    }

    abstract fun getItemTitle(): String

    protected fun startActivityWithTransition(@StringRes transitionNameId: Int, view: View, intent: Intent) {
        val imagePair = view.getPosterImagePair(getString(transitionNameId))
        val options = getActivityOptionsCompat(imagePair)

        window.exitTransition = null
        ActivityCompat.startActivity(this, intent, options?.toBundle())
    }

    override fun onDestroy() {
        performCleanup()
        super.onDestroy()
    }

    protected open fun performCleanup() {
        castAdapter.removeListener()
        crewAdapter.removeListener()
        appBarLayout.removeOnOffsetChangedListener(this)
        sharedElementEnterTransition.removeListener(transitionListener)
    }
}