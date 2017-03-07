package com.ashish.movieguide.ui.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat.START
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import com.ashish.movieguide.R
import com.ashish.movieguide.data.preferences.PreferenceHelper
import com.ashish.movieguide.di.modules.ActivityModule
import com.ashish.movieguide.di.multibindings.AbstractComponent
import com.ashish.movieguide.di.multibindings.activity.ActivityComponentBuilderHost
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilder
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilderHost
import com.ashish.movieguide.ui.base.mvp.MvpActivity
import com.ashish.movieguide.ui.main.TabPagerAdapter.Companion.CONTENT_TYPE_DISCOVER
import com.ashish.movieguide.ui.main.TabPagerAdapter.Companion.CONTENT_TYPE_FAVORITES
import com.ashish.movieguide.ui.main.TabPagerAdapter.Companion.CONTENT_TYPE_MOVIE
import com.ashish.movieguide.ui.main.TabPagerAdapter.Companion.CONTENT_TYPE_PEOPLE
import com.ashish.movieguide.ui.main.TabPagerAdapter.Companion.CONTENT_TYPE_RATED
import com.ashish.movieguide.ui.main.TabPagerAdapter.Companion.CONTENT_TYPE_TV_SHOW
import com.ashish.movieguide.ui.main.TabPagerAdapter.Companion.CONTENT_TYPE_WATCHLIST
import com.ashish.movieguide.ui.multisearch.activity.MultiSearchActivity
import com.ashish.movieguide.ui.widget.FontTextView
import com.ashish.movieguide.utils.CustomTabsHelper
import com.ashish.movieguide.utils.CustomTypefaceSpan
import com.ashish.movieguide.utils.DialogUtils
import com.ashish.movieguide.utils.FontUtils
import com.ashish.movieguide.utils.extensions.applyText
import com.ashish.movieguide.utils.extensions.bindView
import com.ashish.movieguide.utils.extensions.changeMenuAndSubMenuFont
import com.ashish.movieguide.utils.extensions.changeTabFont
import com.ashish.movieguide.utils.extensions.changeViewGroupTextFont
import com.ashish.movieguide.utils.extensions.find
import com.ashish.movieguide.utils.extensions.getStringArray
import com.ashish.movieguide.utils.extensions.loadGravatarImage
import com.ashish.movieguide.utils.extensions.runDelayed
import com.ashish.movieguide.utils.extensions.setVisibility
import com.ashish.movieguide.utils.extensions.showToast
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Provider

class MainActivity : MvpActivity<MainView, MainPresenter>(), MainView, FragmentComponentBuilderHost {

    @Inject lateinit var dialogUtils: Lazy<DialogUtils>
    @Inject lateinit var preferenceHelper: PreferenceHelper

    @Inject
    lateinit var componentBuilders: Map<Class<out Fragment>, @JvmSuppressWildcards Provider<FragmentComponentBuilder<*, *>>>

    private val viewPager: ViewPager by bindView(R.id.view_pager)
    private val tabLayout: TabLayout by bindView(R.id.tab_layout)
    private val drawerLayout: DrawerLayout by bindView(R.id.drawer_layout)
    private val navigationView: NavigationView by bindView(R.id.navigation_view)

    private lateinit var userImage: ImageView
    private lateinit var nameText: FontTextView
    private lateinit var userNameText: FontTextView

    private lateinit var toolbarTitles: Array<String>
    private lateinit var movieTabTitles: Array<String>
    private lateinit var tvShowTabTitles: Array<String>
    private lateinit var peopleTabTitles: Array<String>
    private lateinit var discoverTabTitles: Array<String>
    private lateinit var ratedTabTitles: Array<String>

    private lateinit var tabPagerAdapter: TabPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_TransparentStatusBar)
        super.onCreate(savedInstanceState)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        }

        peopleTabTitles = arrayOf(getString(R.string.popular_txt))
        toolbarTitles = getStringArray(R.array.toolbar_title_array)
        movieTabTitles = getStringArray(R.array.movie_list_type_array)
        tvShowTabTitles = getStringArray(R.array.tv_show_list_type_array)
        discoverTabTitles = getStringArray(R.array.discover_type_array)
        ratedTabTitles = getStringArray(R.array.rated_type_array)

        initDrawerHeader()
        showViewPagerFragment(CONTENT_TYPE_MOVIE, movieTabTitles)
        tabLayout.setupWithViewPager(viewPager)

        tabLayout.changeTabFont()
        toolbar?.changeViewGroupTextFont()
        setupNavigationView()
    }

    override fun injectDependencies(builderHost: ActivityComponentBuilderHost) {
        builderHost.getActivityComponentBuilder(MainActivity::class.java, MainComponent.Builder::class.java)
                .withModule(ActivityModule(this))
                .build()
                .inject(this)
    }

    override fun <F : Fragment, B : FragmentComponentBuilder<F, AbstractComponent<F>>>
            getFragmentComponentBuilder(fragmentKey: Class<F>, builderType: Class<B>): B {
        return builderType.cast(componentBuilders[fragmentKey]!!.get())
    }

    override fun getLayoutId() = R.layout.activity_main

    private fun initDrawerHeader() {
        navigationView.getHeaderView(0).apply {
            nameText = find<FontTextView>(R.id.name_text)
            userImage = find<ImageView>(R.id.user_image)
            userNameText = find<FontTextView>(R.id.user_name_text)
        }

        showUserProfile()
        userImage.setOnClickListener {
            drawerLayout.closeDrawers()
            runDelayed(200L) { showTmdbLoginDialog() }
        }
    }

    private fun showUserProfile() {
        preferenceHelper.apply {
            if (getId() > 0L) {
                nameText.applyText(getName())
                userNameText.applyText(getUserName())
                userImage.loadGravatarImage(getGravatarHash())
            }
        }
    }

    private fun showTmdbLoginDialog() {
        dialogUtils.get().buildDialog()
                .withTitle(R.string.title_tmdb_login)
                .withContent(R.string.content_tmdb_login)
                .withNegativeButton(android.R.string.cancel)
                .withPositiveButton(R.string.login_btn, { presenter?.createRequestToken() })
                .show()
    }

    private fun showViewPagerFragment(contentType: Int, titleArray: Array<String>) {
        supportActionBar?.title = toolbarTitles[contentType]
        tabLayout.setVisibility(contentType != CONTENT_TYPE_PEOPLE)

        tabPagerAdapter = TabPagerAdapter(contentType, supportFragmentManager, titleArray)
        viewPager.apply {
            adapter = tabPagerAdapter
            offscreenPageLimit = tabPagerAdapter.count - 1
        }

        if (titleArray.size <= 3) {
            tabLayout.tabMode = TabLayout.MODE_FIXED
        } else {
            tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        }
    }

    private fun setupNavigationView() {
        val typeface = FontUtils.getTypeface(this, FontUtils.MONTSERRAT_REGULAR)
        val customTypefaceSpan = CustomTypefaceSpan(typeface)
        navigationView.menu.changeMenuAndSubMenuFont(customTypefaceSpan)

        navigationView.setNavigationItemSelectedListener { item ->
            item.isChecked = true
            drawerLayout.closeDrawers()

            when (item.itemId) {
                R.id.drawer_movies -> showViewPagerFragment(CONTENT_TYPE_MOVIE, movieTabTitles)
                R.id.drawer_tv_shows -> showViewPagerFragment(CONTENT_TYPE_TV_SHOW, tvShowTabTitles)
                R.id.drawer_people -> showViewPagerFragment(CONTENT_TYPE_PEOPLE, peopleTabTitles)
                R.id.drawer_discover -> showViewPagerFragment(CONTENT_TYPE_DISCOVER, discoverTabTitles)
                R.id.drawer_favorites -> showViewPagerFragment(CONTENT_TYPE_FAVORITES, discoverTabTitles)
                R.id.drawer_watchlist -> showViewPagerFragment(CONTENT_TYPE_WATCHLIST, discoverTabTitles)
                R.id.drawer_rated -> showViewPagerFragment(CONTENT_TYPE_RATED, ratedTabTitles)
            }

            tabLayout.changeTabFont()
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CustomTabsHelper.RC_TMDB_LOGIN) {
            presenter?.createSession()
        }
    }

    override fun showProgressDialog(messageId: Int) {
        dialogUtils.get().showProgressDialog(messageId)
    }

    override fun hideProgressDialog() {
        dialogUtils.get().dismissProgressDialog()
    }

    override fun validateRequestToken(tokenValidationUrl: String) {
        CustomTabsHelper.launchUrlForResult(this, tokenValidationUrl)
    }

    override fun onLoginSuccess() {
        showUserProfile()
        showToast(String.format(getString(R.string.success_tmdb_login), preferenceHelper.getName()))
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        dialogUtils.get().dismissAllDialogs()
        super.onConfigurationChanged(newConfig)
    }

    override fun onStop() {
        dialogUtils.get().dismissAllDialogs()
        super.onStop()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> performAction { drawerLayout.openDrawer(START) }

        R.id.action_search -> performAction {
            startActivity(Intent(this, MultiSearchActivity::class.java))
            overridePendingTransition(0, 0)
        }

        else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(START)) {
            drawerLayout.closeDrawer(START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        viewPager.adapter = null
        navigationView.setNavigationItemSelectedListener(null)
        super.onDestroy()
    }
}