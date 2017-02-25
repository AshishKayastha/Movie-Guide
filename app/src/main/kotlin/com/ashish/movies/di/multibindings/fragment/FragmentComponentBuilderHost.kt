package com.ashish.movies.di.multibindings.fragment

import android.support.v4.app.Fragment
import com.ashish.movies.di.multibindings.AbstractComponent

interface FragmentComponentBuilderHost {

    fun <F : Fragment, B : FragmentComponentBuilder<F, AbstractComponent<F>>>
            getFragmentComponentBuilder(fragmentKey: Class<F>, builderType: Class<B>): B
}