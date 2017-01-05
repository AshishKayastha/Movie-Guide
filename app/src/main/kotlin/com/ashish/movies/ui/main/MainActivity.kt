package com.ashish.movies.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.view.GravityCompat.START
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TypefaceSpan
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.TextView
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.ui.base.common.BaseActivity
import com.ashish.movies.ui.main.TabPagerAdapter.Companion.CONTENT_TYPE_MOVIE
import com.ashish.movies.ui.main.TabPagerAdapter.Companion.CONTENT_TYPE_PEOPLE
import com.ashish.movies.ui.main.TabPagerAdapter.Companion.CONTENT_TYPE_TV_SHOW
import com.ashish.movies.ui.multisearch.MultiSearchActivity
import com.ashish.movies.utils.CustomTypefaceSpan
import com.ashish.movies.utils.FontUtils
import com.ashish.movies.utils.FontUtils.MONTSERRAT_REGULAR
import com.ashish.movies.utils.extensions.changeTypeface

class MainActivity : BaseActivity() {

    private val viewPager: ViewPager by bindView(R.id.view_pager)
    private val tabLayout: TabLayout by bindView(R.id.tab_layout)
    private val drawerLayout: DrawerLayout by bindView(R.id.drawer_layout)
    private val navigationView: NavigationView by bindView(R.id.navigation_view)

    private lateinit var toolbarTitles: Array<String>
    private lateinit var movieTabTitles: Array<String>
    private lateinit var tvShowTabTitles: Array<String>
    private lateinit var peopleTabTitles: Array<String>

    private lateinit var tabPagerAdapter: TabPagerAdapter
    private lateinit var customTypefaceSpan: TypefaceSpan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
            setDisplayHomeAsUpEnabled(true)
        }

        val typeface = FontUtils.getTypeface(this, MONTSERRAT_REGULAR)
        customTypefaceSpan = CustomTypefaceSpan(typeface)

        toolbarTitles = resources.getStringArray(R.array.toolbar_title_array)
        movieTabTitles = resources.getStringArray(R.array.movie_list_type_array)
        tvShowTabTitles = resources.getStringArray(R.array.tv_show_list_type_array)
        peopleTabTitles = arrayOf(getString(R.string.popular_txt))

        replaceFragment(CONTENT_TYPE_MOVIE, movieTabTitles)
        tabLayout.setupWithViewPager(viewPager)

        changeTabFont()
        changeNavigationMenuFont()
        changeTextViewFont(toolbar!!)

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
                .forEach { changeTextViewFont(it) }
    }

    private fun changeNavigationMenuFont() {
        val size = navigationView.menu.size()
        (0..size - 1)
                .map { navigationView.menu.getItem(it) }
                .filterNotNull()
                .forEach { menuItem ->
                    val spannableString = SpannableString(menuItem.title)
                    spannableString.setSpan(customTypefaceSpan, 0, spannableString.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    menuItem.title = spannableString
                }
    }

    private fun changeTextViewFont(viewGroup: ViewGroup) {
        val size = viewGroup.childCount
        (0..size - 1)
                .map { viewGroup.getChildAt(it) }
                .filterIsInstance<TextView>()
                .forEach(TextView::changeTypeface)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            drawerLayout.openDrawer(START)
            return true
        } else if (item?.itemId == R.id.action_search) {
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