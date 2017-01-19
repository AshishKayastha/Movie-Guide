package com.ashish.movies.ui.main

import android.content.Intent
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
import com.ashish.movies.ui.base.common.BaseActivity
import com.ashish.movies.ui.main.TabPagerAdapter.Companion.CONTENT_TYPE_DISCOVER
import com.ashish.movies.ui.main.TabPagerAdapter.Companion.CONTENT_TYPE_MOVIE
import com.ashish.movies.ui.main.TabPagerAdapter.Companion.CONTENT_TYPE_PEOPLE
import com.ashish.movies.ui.main.TabPagerAdapter.Companion.CONTENT_TYPE_TV_SHOW
import com.ashish.movies.ui.multisearch.MultiSearchActivity
import com.ashish.movies.utils.CustomTypefaceSpan
import com.ashish.movies.utils.FontUtils
import com.ashish.movies.utils.extensions.changeMenuFont
import com.ashish.movies.utils.extensions.changeViewGroupTextFont
import com.ashish.movies.utils.extensions.setVisibility

class MainActivity : BaseActivity() {

    private val viewPager: ViewPager by bindView(R.id.view_pager)
    private val tabLayout: TabLayout by bindView(R.id.tab_layout)
    private val drawerLayout: DrawerLayout by bindView(R.id.drawer_layout)
    private val navigationView: NavigationView by bindView(R.id.navigation_view)

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
            setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
            setDisplayHomeAsUpEnabled(true)
        }

        peopleTabTitles = arrayOf(getString(R.string.popular_txt))
        toolbarTitles = resources.getStringArray(R.array.toolbar_title_array)
        movieTabTitles = resources.getStringArray(R.array.movie_list_type_array)
        tvShowTabTitles = resources.getStringArray(R.array.tv_show_list_type_array)
        discoverTabTitles = resources.getStringArray(R.array.discover_type_array)

        showViewPagerFragment(CONTENT_TYPE_MOVIE, movieTabTitles)
        tabLayout.setupWithViewPager(viewPager)

        changeTabFont()
        toolbar?.changeViewGroupTextFont()

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

    override fun getLayoutId() = R.layout.activity_main

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
        (0..vg.childCount - 1)
                .map { vg.getChildAt(it) as ViewGroup }
                .forEach { it.changeViewGroupTextFont() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            drawerLayout.openDrawer(START)
            return true
        } else if (item.itemId == R.id.action_search) {
            startActivity(Intent(this, MultiSearchActivity::class.java))
            return true
        }

        return super.onOptionsItemSelected(item)
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