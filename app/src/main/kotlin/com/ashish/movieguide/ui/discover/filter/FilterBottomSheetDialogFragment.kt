package com.ashish.movieguide.ui.discover.filter

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import com.ashish.movieguide.R
import com.ashish.movieguide.app.MovieGuideApp
import com.ashish.movieguide.data.models.tmdb.FilterQuery
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilderHost
import com.ashish.movieguide.ui.widget.FontButton
import com.ashish.movieguide.ui.widget.FontTextView
import com.ashish.movieguide.ui.widget.ItemOffsetDecoration
import com.ashish.movieguide.utils.Constants.DATE_PICKER_FORMAT
import com.ashish.movieguide.utils.TMDbConstants.SORT_BY_MOVIE
import com.ashish.movieguide.utils.TMDbConstants.SORT_BY_TV_SHOW
import com.ashish.movieguide.utils.extensions.bindView
import com.ashish.movieguide.utils.extensions.convertToDate
import com.ashish.movieguide.utils.extensions.dpToPx
import com.ashish.movieguide.utils.extensions.get
import com.ashish.movieguide.utils.extensions.getExtrasOrRestore
import com.ashish.movieguide.utils.extensions.getFormattedDate
import com.ashish.movieguide.utils.extensions.hide
import com.ashish.movieguide.utils.extensions.isValidDate
import com.ashish.movieguide.utils.extensions.showToast
import icepick.Icepick
import icepick.State
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * Created by Ashish on Jan 07.
 */
class FilterBottomSheetDialogFragment : BottomSheetDialogFragment() {

    companion object {
        private const val ARG_IS_MOVIE = "is_movie"
        private const val ARG_FILTER_QUERY = "filter_query"

        @JvmStatic
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

    @JvmField @State var isMovie: Boolean = true
    @JvmField @State var filterQuery: FilterQuery? = null

    private val endDateText: FontTextView by bindView(R.id.end_date_text)
    private val applyFilterBtn: FontButton by bindView(R.id.apply_filter_btn)
    private val startDateText: FontTextView by bindView(R.id.start_date_text)
    private val sortByRadioGroup: RadioGroup by bindView(R.id.sort_radio_group)
    private val genreRecyclerView: RecyclerView by bindView(R.id.genre_recycler_view)

    private val calendar = Calendar.getInstance()
    private lateinit var genreAdapter: GenreAdapter
    private var endDatePickerDialog: DatePickerDialog? = null
    private var startDatePickerDialog: DatePickerDialog? = null

    private val startDateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        setFormattedDate(year, month, dayOfMonth, true)
    }

    private val endDateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        setFormattedDate(year, month, dayOfMonth, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (targetFragment as FragmentComponentBuilderHost)
                .getFragmentComponentBuilder(FilterBottomSheetDialogFragment::class.java,
                        FilterComponent.Builder::class.java)
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_filter, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState.getExtrasOrRestore(this) {
            getFragmentArguments()
        }

        if (isMovie) {
            genreAdapter = GenreAdapter(activity, R.array.movie_genre_list, R.array.movie_genre_id_list,
                    filterQuery?.genreIds)
        } else {
            genreAdapter = GenreAdapter(activity, R.array.tv_genre_list, R.array.tv_genre_id_list,
                    filterQuery?.genreIds)
        }

        genreRecyclerView.apply {
            addItemDecoration(ItemOffsetDecoration(8f.dpToPx().toInt()))
            layoutManager = StaggeredGridLayoutManager(2, GridLayoutManager.HORIZONTAL)
            adapter = genreAdapter
        }

        val sortByIndex: Int
        if (isMovie) {
            sortByIndex = SORT_BY_MOVIE.indexOf(filterQuery?.sortBy)

        } else {
            // Hide sort by title RadioButton for TV Shows
            sortByRadioGroup.weightSum = 3f
            sortByRadioGroup[2]?.hide()
            sortByIndex = SORT_BY_TV_SHOW.indexOf(filterQuery?.sortBy)
        }

        (sortByRadioGroup[sortByIndex] as RadioButton?)?.isChecked = true

        applyFilterBtn.setOnClickListener {
            if (isValidDate(filterQuery?.minDate, filterQuery?.maxDate)) {
                updateFilterQuery()
                filterQueryModel.setFilterQuery(filterQuery!!)
                dismiss()
            } else {
                activity.showToast(R.string.error_choose_valid_date)
            }
        }

        initDatePicker(filterQuery?.minDate.convertToDate(), true)
        initDatePicker(filterQuery?.maxDate.convertToDate(), false)

        startDateText.setOnClickListener { startDatePickerDialog?.show() }

        endDateText.setOnClickListener { endDatePickerDialog?.show() }
    }

    private fun getFragmentArguments() {
        arguments.apply {
            isMovie = getBoolean(ARG_IS_MOVIE)
            filterQuery = getParcelable(ARG_FILTER_QUERY)
        }
    }

    private fun initDatePicker(date: Date?, isStartDate: Boolean) {
        if (date != null) calendar.time = date

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        if (isStartDate) {
            startDatePickerDialog = DatePickerDialog(activity, startDateSetListener, year, month, dayOfMonth)
        } else {
            endDatePickerDialog = DatePickerDialog(activity, endDateSetListener, year, month, dayOfMonth)
        }

        if (date != null) {
            setFormattedDate(year, month, dayOfMonth, isStartDate)
        }
    }

    private fun setFormattedDate(year: Int, month: Int, dayOfMonth: Int, isStartDate: Boolean) {
        val datePickerDate = getFormattedDatePickerDate(year, month, dayOfMonth)
        if (isStartDate) {
            filterQuery?.minDate = datePickerDate
            startDateText.text = datePickerDate.getFormattedDate()
        } else {
            filterQuery?.maxDate = datePickerDate
            endDateText.text = datePickerDate.getFormattedDate()
        }
    }

    private fun getFormattedDatePickerDate(year: Int, month: Int, dayOfMonth: Int): String {
        return String.format(Locale.getDefault(), DATE_PICKER_FORMAT, year, month + 1, dayOfMonth)
    }

    private fun updateFilterQuery() {
        val radioButton = sortByRadioGroup.findViewById(sortByRadioGroup.checkedRadioButtonId)
        val sortByIndex = sortByRadioGroup.indexOfChild(radioButton)

        if (isMovie) {
            filterQuery?.sortBy = SORT_BY_MOVIE[sortByIndex]
        } else {
            filterQuery?.sortBy = SORT_BY_TV_SHOW[sortByIndex]
        }

        filterQuery?.genreIds = genreAdapter.getSelectedGenreIds()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        updateFilterQuery()
        Icepick.saveInstanceState(this, outState)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        genreRecyclerView.adapter = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        MovieGuideApp.getRefWatcher(activity).watch(this)
    }
}