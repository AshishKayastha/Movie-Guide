package com.ashish.movieguide.ui.discover.filter

import com.ashish.movieguide.di.multibindings.AbstractComponent
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilder
import dagger.Subcomponent

/**
 * Created by Ashish on Jan 24.
 */
@Subcomponent
interface FilterComponent : AbstractComponent<FilterBottomSheetDialogFragment> {

    @Subcomponent.Builder
    interface Builder : FragmentComponentBuilder<FilterBottomSheetDialogFragment, FilterComponent>
}