package com.ashish.movies.ui.discover.common.filter

import dagger.Subcomponent

/**
 * Created by Ashish on Jan 24.
 */
@Subcomponent
interface FilterSubComponent {

    fun inject(filterBottomSheetDialogFragment: FilterBottomSheetDialogFragment)
}