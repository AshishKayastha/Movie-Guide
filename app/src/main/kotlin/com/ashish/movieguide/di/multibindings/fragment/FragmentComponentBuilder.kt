package com.ashish.movieguide.di.multibindings.fragment

import android.support.v4.app.Fragment
import com.ashish.movieguide.di.multibindings.AbstractComponent
import com.ashish.movieguide.di.multibindings.ComponentBuilder

interface FragmentComponentBuilder<in F : Fragment, out C : AbstractComponent<F>> : ComponentBuilder<F, C>