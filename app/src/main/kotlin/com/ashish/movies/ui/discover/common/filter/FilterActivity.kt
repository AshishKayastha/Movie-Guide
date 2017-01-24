package com.ashish.movies.ui.discover.common.filter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.data.models.FilterQuery
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.ui.base.common.BaseActivity
import com.ashish.movies.ui.widget.FontButton
import com.ashish.movies.ui.widget.GenreLayout
import com.ashish.movies.utils.extensions.changeViewGroupTextFont
import javax.inject.Inject

/**
 * Created by Ashish on Jan 24.
 */
class FilterActivity : BaseActivity() {

    companion object {
        private const val EXTRA_IS_MOVIE = "is_movie"
        private const val EXTRA_FILTER_QUERY = "filter_query"

        fun createIntent(context: Context, isMovie: Boolean, filterQuery: FilterQuery): Intent {
            return Intent(context, FilterActivity::class.java)
                    .putExtra(EXTRA_IS_MOVIE, isMovie)
                    .putExtra(EXTRA_FILTER_QUERY, filterQuery)
        }
    }

    @Inject lateinit var filterQueryModel: FilterQueryModel

    private val applyFilterBtn: FontButton by bindView(R.id.apply_btn)
    private val genreLayout: GenreLayout by bindView(R.id.genre_layout)

    private var isMovie: Boolean = true
    private lateinit var filterQuery: FilterQuery

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.apply {
            setTitle(R.string.title_filter)
            setDisplayHomeAsUpEnabled(true)
        }

        toolbar?.changeViewGroupTextFont()

        genreLayout.setSelectedGenreIds(filterQuery.genreIds)
        if (isMovie) {
            genreLayout.setGenres(R.array.movie_genre_list, R.array.movie_genre_id_list)
        } else {
            genreLayout.setGenres(R.array.tv_genre_list, R.array.tv_genre_id_list)
        }

        applyFilterBtn.setOnClickListener {
            filterQuery.genreIds = genreLayout.getSelectedGenreIds()
            filterQueryModel.setFilterQuery(filterQuery)
            finishAfterTransition()
        }
    }

    override fun injectDependencies(appComponent: AppComponent) {

    }

    override fun getLayoutId() = R.layout.activity_filter

    override fun getIntentExtras(extras: Bundle?) {
        extras!!.apply {
            isMovie = getBoolean(EXTRA_IS_MOVIE)
            filterQuery = getParcelable(EXTRA_FILTER_QUERY)
        }
    }
}