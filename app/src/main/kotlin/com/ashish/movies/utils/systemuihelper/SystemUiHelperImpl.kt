package com.ashish.movies.utils.systemuihelper

import android.support.v7.app.AppCompatActivity
import android.view.View

internal class SystemUiHelperImpl(private val appCompatActivity: AppCompatActivity,
                                  private val level: Int, private val flags: Int,
                                  private var onVisibilityChangeListener: SystemUiHelper.OnVisibilityChangeListener?)
    : View.OnSystemUiVisibilityChangeListener {

    var isShowing = true
    private val decorView: View = appCompatActivity.window.decorView

    init {
        decorView.setOnSystemUiVisibilityChangeListener(this)
    }

    fun show() {
        decorView.systemUiVisibility = createShowFlags()
        decorView.requestLayout()
    }

    fun hide() {
        decorView.systemUiVisibility = createHideFlags()
        decorView.requestLayout()
    }

    override fun onSystemUiVisibilityChange(visibility: Int) {
        if (visibility and createTestFlags() != 0) {
            onSystemUiHidden()
        } else {
            onSystemUiShown()
        }
    }

    private fun onSystemUiShown() {
        if (level == SystemUiHelper.LEVEL_LOW_PROFILE) {
            appCompatActivity.supportActionBar?.show()
        }
        setIsShowing(true)
    }

    private fun onSystemUiHidden() {
        if (level == SystemUiHelper.LEVEL_LOW_PROFILE) {
            appCompatActivity.supportActionBar?.hide()
        }
        setIsShowing(false)
    }

    private fun setIsShowing(isShowing: Boolean) {
        this.isShowing = isShowing
        onVisibilityChangeListener?.onVisibilityChange(this.isShowing)
    }

    private fun createShowFlags(): Int {
        var flag = View.SYSTEM_UI_FLAG_VISIBLE

        if (level >= SystemUiHelper.LEVEL_HIDE_STATUS_BAR) {
            flag = flag or (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

            if (level >= SystemUiHelper.LEVEL_LEAN_BACK) {
                flag = flag or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            }
        }

        return flag
    }

    private fun createTestFlags(): Int {
        if (level >= SystemUiHelper.LEVEL_LEAN_BACK) {
            return View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }

        return View.SYSTEM_UI_FLAG_LOW_PROFILE
    }

    private fun createHideFlags(): Int {
        var flag = View.SYSTEM_UI_FLAG_LOW_PROFILE

        if (level >= SystemUiHelper.LEVEL_LEAN_BACK) {
            flag = flag or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }

        if (level >= SystemUiHelper.LEVEL_HIDE_STATUS_BAR) {
            flag = flag or (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)

            if (level >= SystemUiHelper.LEVEL_LEAN_BACK) {
                flag = flag or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            }
        }

        if (level == SystemUiHelper.LEVEL_IMMERSIVE) {
            flag = flag or if (flags and SystemUiHelper.FLAG_IMMERSIVE_STICKY != 0)
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            else
                View.SYSTEM_UI_FLAG_IMMERSIVE
        }

        return flag
    }

    fun removeVisibilityChangeListener() {
        onVisibilityChangeListener = null
        decorView.setOnSystemUiVisibilityChangeListener(null)
    }
}