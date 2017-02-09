package com.ashish.movies.ui.discover.common.filter

import dagger.Subcomponent

/**
 * Created by Ashish on Jan 24.
 */
@Subcomponent
interface FilterComponent {

    fun inject(filterBottomSheetDialogFragment: FilterBottomSheetDialogFragment)
}