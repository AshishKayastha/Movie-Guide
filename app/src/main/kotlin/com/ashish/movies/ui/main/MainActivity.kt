package com.ashish.movies.ui.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.view.GravityCompat.START
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.data.preferences.PreferenceHelper
import com.ashish.movies.di.components.UiComponent
import com.ashish.movies.ui.base.mvp.MvpActivity
import com.ashish.movies.ui.main.TabPagerAdapter.Companion.CONTENT_TYPE_DISCOVER
import com.ashish.movies.ui.main.TabPagerAdapter.Companion.CONTENT_TYPE_MOVIE
import com.ashish.movies.ui.main.TabPagerAdapter.Companion.CONTENT_TYPE_PEOPLE
import com.ashish.movies.ui.main.TabPagerAdapter.Companion.CONTENT_TYPE_TV_SHOW
import com.ashish.movies.ui.multisearch.MultiSearchActivity
import com.ashish.movies.ui.widget.CircleImageView
import com.ashish.movies.ui.widget.FontTextView
import com.ashish.movies.utils.CustomTabsHelper
import com.ashish.movies.utils.CustomTypefaceSpan
import com.ashish.movies.utils.DialogUtils
import com.ashish.movies.utils.FontUtils
import com.ashish.movies.utils.extensions.applyText
import com.ashish.movies.utils.extensions.changeMenuFont
import com.ashish.movies.utils.extensions.changeViewGroupTextFont
import com.ashish.movies.utils.extensions.find
import com.ashish.movies.utils.extensions.getStringArray
import com.ashish.movies.utils.extensions.isNotNullOrEmpty
import com.ashish.movies.utils.extensions.loadImageUrl
import com.ashish.movies.utils.extensions.runDelayed
import com.ashish.movies.utils.extensions.setVisibility
import com.ashish.movies.utils.extensions.showToast
import dagger.Lazy
import javax.inject.Inject

class MainActivity : MvpActivity<MainView, MainPresenter>(), MainView {

    @Inject lateinit var dialogUtils: Lazy<DialogUtils>
    @Inject lateinit var preferenceHelper: PreferenceHelper

    private val viewPager: ViewPager by bindView(R.id.view_pager)
    private val tabLayout: TabLayout by bindView(R.id.tab_layout)
    private val drawerLayout: DrawerLayout by bindView(R.id.drawer_layout)
    private val navigationView: NavigationView by bindView(R.id.navigation_view)

    private var nameText: FontTextView? = null
    private var userImage: CircleImageView? = null
    private var userNameText: FontTextView? = null

    private lateinit var toolbarTitles: Array<String>
    private lateinit var movieTabTitles: Array<String>
    private lateinit var tvShowTabTitles: Array<String>
    private lateinit var peopleTabTitles: Array<String>
    private lateinit var discoverTabTitles: Array<String>

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

        initDrawerHeader()
        showViewPagerFragment(CONTENT_TYPE_MOVIE, movieTabTitles)
        tabLayout.setupWithViewPager(viewPager)

        changeTabFont()
        toolbar?.changeViewGroupTextFont()
        setupNavigationView()
    }

    override fun injectDependencies(uiComponent: UiComponent) = uiComponent.inject(this)

    override fun getLayoutId() = R.layout.activity_main

    private fun initDrawerHeader() {
        navigationView.getHeaderView(0).apply {
            nameText = find<FontTextView>(R.id.name_text)
            userImage = find<CircleImageView>(R.id.user_image)
            userNameText = find<FontTextView>(R.id.user_name_text)
        }

        showUserProfile()
        userImage?.setOnClickListener {
            drawerLayout.closeDrawers()
            runDelayed(200L) { showTmdbLoginDialog() }
        }
    }

    private fun showUserProfile() {
        preferenceHelper.apply {
            val name = getName()
            if (name.isNotNullOrEmpty()) {
                nameText?.applyText(name)
            } else {
                nameText?.setText(R.string.login_with_tmdb)
            }

            val userName = getUserName()
            userNameText?.applyText(userName)
            userImage?.isClickable = userName.isNullOrEmpty()

            val gravatarHash = getGravatarHash()
            if (gravatarHash.isNotNullOrEmpty()) {
                userImage?.loadImageUrl("https://www.gravatar.com/avatar/$gravatarHash.jpg?s=90")
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

        if (contentType == CONTENT_TYPE_DISCOVER) {
            tabLayout.tabMode = TabLayout.MODE_FIXED
        } else {
            tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        }
    }

    private fun changeTabFont() {
        val vg = tabLayout.getChildAt(0) as ViewGroup
        (0 until vg.childCount)
                .map { vg.getChildAt(it) as ViewGroup }
                .forEach { it.changeViewGroupTextFont() }
    }

    private fun setupNavigationView() {
        val typeface = FontUtils.getTypeface(this, FontUtils.MONTSERRAT_REGULAR)
        val customTypefaceSpan = CustomTypefaceSpan(typeface)
        navigationView.menu.changeMenuFont(customTypefaceSpan)

        navigationView.setNavigationItemSelectedListener { item ->
            item.isChecked = true
            drawerLayout.closeDrawers()

            when (item.itemId) {
                R.id.action_movies -> showViewPagerFragment(CONTENT_TYPE_MOVIE, movieTabTitles)
                R.id.action_tv_shows -> showViewPagerFragment(CONTENT_TYPE_TV_SHOW, tvShowTabTitles)
                R.id.action_people -> showViewPagerFragment(CONTENT_TYPE_PEOPLE, peopleTabTitles)
                R.id.action_discover -> showViewPagerFragment(CONTENT_TYPE_DISCOVER, discoverTabTitles)
            }

            changeTabFont()
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