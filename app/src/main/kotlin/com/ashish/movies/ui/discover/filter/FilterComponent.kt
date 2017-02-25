package com.ashish.movies.ui.discover.filter

import com.ashish.movies.di.multibindings.AbstractComponent
import com.ashish.movies.di.multibindings.fragment.FragmentComponentBuilder
import dagger.Subcomponent

/**
 * Created by Ashish on Jan 24.
 */
@Subcomponent
interface FilterComponent : AbstractComponent<FilterBottomSheetDialogFragment> {

    @Subcomponent.Builder
    interface Builder : FragmentComponentBuilder<FilterBottomSheetDialogFragment, FilterComponent>
}