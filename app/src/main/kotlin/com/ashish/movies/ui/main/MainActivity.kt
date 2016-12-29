package com.ashish.movies.ui.main

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import com.ashish.movies.R
import com.ashish.movies.ui.base.common.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private var movieTabTitles: Array<String>? = null
    private var tabPagerAdapter: TabPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        actionBar?.setDisplayHomeAsUpEnabled(true)

        movieTabTitles = resources.getStringArray(R.array.movie_list_type_array)
        tabPagerAdapter = TabPagerAdapter(supportFragmentManager, movieTabTitles!!)
        viewPager.adapter = tabPagerAdapter
        tabLayout.setupWithViewPager(viewPager)

        navigationView.setNavigationItemSelectedListener { item ->
            item.isChecked = true
            drawerLayout.closeDrawers()
            true
        }
    }

    override fun getLayoutId() = R.layout.activity_main

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
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