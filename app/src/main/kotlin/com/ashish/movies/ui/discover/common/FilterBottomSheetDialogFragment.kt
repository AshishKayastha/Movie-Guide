package com.ashish.movies.ui.discover.common

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.app.MoviesApp
import com.ashish.movies.data.models.FilterQuery
import com.ashish.movies.ui.widget.FontButton
import com.ashish.movies.ui.widget.GenreLayout
import icepick.Icepick
import javax.inject.Inject

/**
 * Created by Ashish on Jan 07.
 */
class FilterBottomSheetDialogFragment : BottomSheetDialogFragment() {

    companion object {
        private const val ARG_IS_MOVIE = "is_movie"
        private const val ARG_FILTER_QUERY = "filter_query"

        fun newInstance(isMovie: Boolean, filterQuery: FilterQuery): FilterBottomSheetDialogFragment {
            val args = Bundle()
            args.putBoolean(ARG_IS_MOVIE, isMovie)
            args.putParcelable(ARG_FILTER_QUERY, filterQuery)
            val fragment = FilterBottomSheetDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject lateinit var filterQueryModel: FilterQueryModel

    private val applyFilterBtn: FontButton by bindView(R.id.apply_btn)
    private val genreLayout: GenreLayout by bindView(R.id.genre_layout)

    private var isMovie: Boolean = true
    private lateinit var filterQuery: FilterQuery

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MoviesApp.getAppComponent(activity).plus(DiscoverModule(activity)).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_filter_movies, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            getFragmentArguments()
        } else {
            Icepick.restoreInstanceState(this, savedInstanceState)
        }

        genreLayout.setSelectedGenreIds(filterQuery.genreIds)
        if (isMovie) {
            genreLayout.setGenres(R.array.movie_genre_list, R.array.movie_genre_id_list)
        } else {
            genreLayout.setGenres(R.array.tv_genre_list, R.array.tv_genre_id_list)
        }

        applyFilterBtn.setOnClickListener {
            filterQuery.genreIds = genreLayout.getSelectedGenreIds()
            filterQueryModel.setFilterQuery(filterQuery)
            dismiss()
        }
    }

    private fun getFragmentArguments() {
        isMovie = arguments.getBoolean(ARG_IS_MOVIE)
        filterQuery = arguments.getParcelable(ARG_FILTER_QUERY)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        Icepick.saveInstanceState(this, outState)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        MoviesApp.getRefWatcher(activity).watch(this)
    }
}