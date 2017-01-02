package com.ashish.movies.ui.main

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.view.GravityCompat.START
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.TextView
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.ui.base.common.BaseActivity
import com.ashish.movies.ui.main.TabPagerAdapter.Companion.CONTENT_TYPE_MOVIE
import com.ashish.movies.ui.main.TabPagerAdapter.Companion.CONTENT_TYPE_PEOPLE
import com.ashish.movies.ui.main.TabPagerAdapter.Companion.CONTENT_TYPE_TV_SHOW
import com.ashish.movies.utils.extensions.changeTypeface

class MainActivity : BaseActivity() {

    val viewPager: ViewPager by bindView(R.id.view_pager)
    val tabLayout: TabLayout by bindView(R.id.tab_layout)
    val drawerLayout: DrawerLayout by bindView(R.id.drawer_layout)
    val navigationView: NavigationView by bindView(R.id.navigation_view)

    private lateinit var toolbarTitles: Array<String>
    private lateinit var movieTabTitles: Array<String>
    private lateinit var tvShowTabTitles: Array<String>
    private lateinit var peopleTabTitles: Array<String>

    private lateinit var tabPagerAdapter: TabPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
            setDisplayHomeAsUpEnabled(true)
        }

        toolbarTitles = resources.getStringArray(R.array.toolbar_title_array)
        movieTabTitles = resources.getStringArray(R.array.movie_list_type_array)
        tvShowTabTitles = resources.getStringArray(R.array.tv_show_list_type_array)
        peopleTabTitles = arrayOf(getString(R.string.popular_txt))

        replaceFragment(CONTENT_TYPE_MOVIE, movieTabTitles)
        tabLayout.setupWithViewPager(viewPager)
        changeTabFont()

        navigationView.setNavigationItemSelectedListener { item ->
            item.isChecked = true
            drawerLayout.closeDrawers()

            when (item.itemId) {
                R.id.action_movies -> replaceFragment(CONTENT_TYPE_MOVIE, movieTabTitles)
                R.id.action_tv_shows -> replaceFragment(CONTENT_TYPE_TV_SHOW, tvShowTabTitles)
                R.id.action_people -> replaceFragment(CONTENT_TYPE_PEOPLE, peopleTabTitles)
            }

            changeTabFont()
            true
        }
    }

    override fun getLayoutId() = R.layout.activity_main

    private fun replaceFragment(contentType: Int, titleArray: Array<String>) {
        supportActionBar?.title = toolbarTitles[contentType]
        tabPagerAdapter = TabPagerAdapter(contentType, supportFragmentManager, titleArray)
        viewPager.apply {
            adapter = tabPagerAdapter
            offscreenPageLimit = tabPagerAdapter.count - 1
        }
    }

    private fun changeTabFont() {
        val vg = tabLayout.getChildAt(0) as ViewGroup
        (0..vg.childCount - 1)
                .map { vg.getChildAt(it) as ViewGroup }
                .forEach { vgTab ->
                    (0..vgTab.childCount - 1)
                            .map { vgTab.getChildAt(it) }
                            .filterIsInstance<TextView>()
                            .forEach { it.changeTypeface() }
                }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            drawerLayout.openDrawer(START)
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
}