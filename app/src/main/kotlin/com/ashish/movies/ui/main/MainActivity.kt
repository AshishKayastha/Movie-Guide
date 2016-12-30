package com.ashish.movies.ui.main

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.View
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.ui.base.common.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    val viewPager: ViewPager by bindView(R.id.view_pager)
    val tabLayout: TabLayout by bindView(R.id.tab_layout)
    val drawerLayout: DrawerLayout by bindView(R.id.drawer_layout)
    val navigationView: NavigationView by bindView(R.id.navigation_view)

    private lateinit var movieTabTitles: Array<String>
    private lateinit var tvShowTabTitles: Array<String>

    private lateinit var tabPagerAdapter: TabPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        actionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
            setDisplayHomeAsUpEnabled(true)
        }

        val actionBarDrawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_drawer, R.string.close_drawer) {
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }
        }

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        movieTabTitles = resources.getStringArray(R.array.movie_list_type_array)
        tvShowTabTitles = resources.getStringArray(R.array.tv_show_list_type_array)

        tabPagerAdapter = TabPagerAdapter(supportFragmentManager, movieTabTitles)
        viewPager.apply {
            adapter = tabPagerAdapter
            offscreenPageLimit = 2
        }

        tabLayout.setupWithViewPager(viewPager)

        navigationView.setNavigationItemSelectedListener { item ->
            item.isChecked = true
            drawerLayout.closeDrawers()
            when (item.itemId) {
                R.id.action_movies -> tabPagerAdapter.updateTabTitles(movieTabTitles)
                R.id.action_tv_shows -> tabPagerAdapter.updateTabTitles(tvShowTabTitles)
            }
            true
        }
    }

    override fun getLayoutId() = R.layout.activity_main

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId === android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}