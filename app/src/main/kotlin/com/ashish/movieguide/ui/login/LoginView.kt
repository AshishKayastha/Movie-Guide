package com.ashish.movieguide.ui.login

import com.ashish.movieguide.ui.base.mvp.MvpView

interface LoginView : MvpView {

    fun onLoginSuccess()

    fun onLoginError()
}