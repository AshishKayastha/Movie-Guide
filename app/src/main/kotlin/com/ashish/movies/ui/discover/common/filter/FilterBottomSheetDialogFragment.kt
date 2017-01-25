package com.ashish.movies.ui.discover.common.filter

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.app.MoviesApp
import com.ashish.movies.data.models.FilterQuery
import com.ashish.movies.ui.discover.common.BaseDiscoverFragment
import com.ashish.movies.ui.widget.FontButton
import com.ashish.movies.ui.widget.FontTextView
import com.ashish.movies.ui.widget.ItemOffsetDecoration
import com.ashish.movies.utils.Constants.SORT_BY_MOVIE
import com.ashish.movies.utils.extensions.dpToPx
import icepick.Icepick
import java.util.*
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

    private val endDateText: FontTextView by bindView(R.id.end_date_text)
    private val applyFilterBtn: FontButton by bindView(R.id.apply_filter_btn)
    private val startDateText: FontTextView by bindView(R.id.start_date_text)
    private val sortByRadioGroup: RadioGroup by bindView(R.id.sort_radio_group)
    private val genreRecyclerView: RecyclerView by bindView(R.id.genre_recycler_view)

    private var isMovie: Boolean = true
    private lateinit var filterQuery: FilterQuery
    private var behavior: BottomSheetBehavior<View>? = null

    private lateinit var genreAdapter: GenreAdapter
    private var endDatePickerDialog: DatePickerDialog? = null
    private var startDatePickerDialog: DatePickerDialog? = null

    private val startDateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        startDateText.text = "$year-${month + 1}-$dayOfMonth"
    }

    private val endDateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        endDateText.text = "$year-${month + 1}-$dayOfMonth"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (targetFragment is BaseDiscoverFragment<*, *>) {
            (targetFragment as BaseDiscoverFragment<*, *>).discoverComponent.createFilterComponent().inject(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_filter, container, false)
//        behavior = BottomSheetBehavior.from(view)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            getFragmentArguments()
        } else {
            Icepick.restoreInstanceState(this, savedInstanceState)
        }

        if (isMovie) {
            genreAdapter = GenreAdapter(activity, R.array.movie_genre_list, R.array.movie_genre_id_list,
                    filterQuery.genreIds)
        } else {
            genreAdapter = GenreAdapter(activity, R.array.tv_genre_list, R.array.tv_genre_id_list,
                    filterQuery.genreIds)
        }

        genreRecyclerView.apply {
            addItemDecoration(ItemOffsetDecoration(6f.dpToPx().toInt()))
            layoutManager = StaggeredGridLayoutManager(3, GridLayoutManager.HORIZONTAL)
            adapter = genreAdapter
        }

        val sortByIndex = SORT_BY_MOVIE.indexOf(filterQuery.sortBy)
        (sortByRadioGroup.getChildAt(sortByIndex) as RadioButton?)?.isChecked = true

        applyFilterBtn.setOnClickListener {
            val radioButton = sortByRadioGroup.findViewById(sortByRadioGroup.checkedRadioButtonId)
            val index = sortByRadioGroup.indexOfChild(radioButton)

            filterQuery.sortBy = SORT_BY_MOVIE[index]
            filterQuery.minDate = startDateText.text.toString()
            filterQuery.maxDate = endDateText.text.toString()
            filterQuery.genreIds = genreAdapter.getSelectedGenreIds()
            filterQueryModel.setFilterQuery(filterQuery)
            dismiss()
        }

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        startDateText.text = "$year-${month + 1}-$dayOfMonth"
        endDateText.text = "$year-${month + 1}-$dayOfMonth"

        startDatePickerDialog = DatePickerDialog(activity, startDateSetListener, year, month, dayOfMonth)
        endDatePickerDialog = DatePickerDialog(activity, endDateSetListener, year, month, dayOfMonth)

        startDateText.setOnClickListener { startDatePickerDialog?.show() }

        endDateText.setOnClickListener { endDatePickerDialog?.show() }
    }

    private fun getFragmentArguments() {
        arguments.apply {
            isMovie = getBoolean(ARG_IS_MOVIE)
            filterQuery = getParcelable(ARG_FILTER_QUERY)
        }
    }

    override fun onStart() {
        super.onStart()
        behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        Icepick.saveInstanceState(this, outState)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        genreRecyclerView.adapter = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        MoviesApp.getRefWatcher(activity).watch(this)
    }
}