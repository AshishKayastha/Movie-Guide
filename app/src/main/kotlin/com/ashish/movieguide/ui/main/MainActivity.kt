package com.ashish.movieguide.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.util.Pair
import android.support.v4.view.GravityCompat.START
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import com.ashish.movieguide.R
import com.ashish.movieguide.data.preferences.PreferenceHelper
import com.ashish.movieguide.di.modules.ActivityModule
import com.ashish.movieguide.di.multibindings.AbstractComponent
import com.ashish.movieguide.di.multibindings.activity.ActivityComponentBuilderHost
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilder
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilderHost
import com.ashish.movieguide.ui.base.common.BaseActivity
import com.ashish.movieguide.ui.login.LoginActivity
import com.ashish.movieguide.ui.main.TabFragmentFactory.CONTENT_TYPE_DISCOVER
import com.ashish.movieguide.ui.main.TabFragmentFactory.CONTENT_TYPE_FAVORITES
import com.ashish.movieguide.ui.main.TabFragmentFactory.CONTENT_TYPE_MOVIE
import com.ashish.movieguide.ui.main.TabFragmentFactory.CONTENT_TYPE_PEOPLE
import com.ashish.movieguide.ui.main.TabFragmentFactory.CONTENT_TYPE_RATED
import com.ashish.movieguide.ui.main.TabFragmentFactory.CONTENT_TYPE_TV_SHOW
import com.ashish.movieguide.ui.main.TabFragmentFactory.CONTENT_TYPE_WATCHLIST
import com.ashish.movieguide.ui.multisearch.activity.MultiSearchActivity
import com.ashish.movieguide.ui.profile.ProfileActivity
import com.ashish.movieguide.ui.widget.FontTextView
import com.ashish.movieguide.utils.CustomTypefaceSpan
import com.ashish.movieguide.utils.FontUtils
import com.ashish.movieguide.utils.extensions.applyText
import com.ashish.movieguide.utils.extensions.changeMenuAndSubMenuFont
import com.ashish.movieguide.utils.extensions.changeTabFont
import com.ashish.movieguide.utils.extensions.changeViewGroupTextFont
import com.ashish.movieguide.utils.extensions.find
import com.ashish.movieguide.utils.extensions.getStringArray
import com.ashish.movieguide.utils.extensions.loadCircularImage
import com.ashish.movieguide.utils.extensions.loadImage
import com.ashish.movieguide.utils.extensions.runDelayed
import com.ashish.movieguide.utils.extensions.setVisibility
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import javax.inject.Provider

class MainActivity : BaseActivity(), FragmentComponentBuilderHost {

    companion object {
        private const val RC_CONNECT_TRAKT = 99
    }

    @Inject lateinit var preferenceHelper: PreferenceHelper

    @Inject
    lateinit var componentBuilders: Map<Class<out Fragment>, @JvmSuppressWildcards Provider<FragmentComponentBuilder<*, *>>>

    private lateinit var userImage: ImageView
    private lateinit var headerImage: ImageView
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

        supportActionBar?.run {
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
        showViewPagerFragment(CONTENT_TYPE_DISCOVER, discoverTabTitles)
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
        navigationView.getHeaderView(0).run {
            nameText = find(R.id.name_text)
            userImage = find(R.id.user_image)
            headerImage = find(R.id.header_bg_image)
            userNameText = find(R.id.user_name_text)
        }

        showUserProfile()
        userImage.setOnClickListener {
            if (preferenceHelper.isLoggedIn()) {
                startProfileActivity()
            } else {
                startLoginActivity()
            }
        }
    }

    private fun startLoginActivity() {
        drawerLayout.closeDrawers()
        runDelayed(250L) {
            startActivityForResult(Intent(this, LoginActivity::class.java), RC_CONNECT_TRAKT)
        }
    }

    private fun startProfileActivity() {
        val viewPair = Pair.create(userImage as View, getString(R.string.transition_user_image))
        val intent = Intent(this, ProfileActivity::class.java)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, viewPair)
        window.exitTransition = null
        ActivityCompat.startActivityForResult(this, intent, RC_CONNECT_TRAKT, options.toBundle())
    }

    private fun showUserProfile() {
        preferenceHelper.run {
            if (isLoggedIn()) {
                nameText.applyText(getName())
            } else {
                nameText.setText(R.string.connect_to_trakt)
            }

            userNameText.applyText(getUserName())
            userImage.loadCircularImage(getImageUrl())
            headerImage.loadImage(getCoverImageUrl())
        }
    }

    private fun showViewPagerFragment(contentType: Int, titleArray: Array<String>) {
        supportActionBar?.title = toolbarTitles[contentType]
        tabLayout.setVisibility(contentType != CONTENT_TYPE_PEOPLE)

        tabPagerAdapter = TabPagerAdapter(contentType, supportFragmentManager, titleArray)
        viewPager.run {
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
        if (requestCode == RC_CONNECT_TRAKT && resultCode == Activity.RESULT_OK) {
            showUserProfile()
        }
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