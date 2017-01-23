package com.ashish.movies.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.support.annotation.ArrayRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ashish.movies.R
import com.ashish.movies.utils.extensions.dpToPx
import com.wefika.flowlayout.FlowLayout
import java.util.*

/**
 * Created by Ashish on Jan 23.
 */
class GenreLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
    : FlowLayout(context, attrs) {

    companion object {
        @JvmStatic private val DEFAULT_SPACING: Int = 8f.dpToPx().toInt()
        @JvmStatic private val DEFAULT_HEIGHT: Int = 36f.dpToPx().toInt()
    }

    private val itemHeight: Int
    private val itemSpacing: Int
    private var selectedGenreIdList = ArrayList<String>()
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.GenreLayout)
        itemHeight = ta.getDimensionPixelSize(R.styleable.GenreLayout_itemHeight, DEFAULT_HEIGHT)
        itemSpacing = ta.getDimensionPixelSize(R.styleable.GenreLayout_itemSpacing, DEFAULT_SPACING)
        ta.recycle()
    }

    fun setGenres(@ArrayRes genreArrayId: Int, @ArrayRes genreIdArrayId: Int) {
        val resources = context.resources
        val genres = resources.getStringArray(genreArrayId)
        val genreIds = resources.getStringArray(genreIdArrayId)
        genres.forEachIndexed { i, genre -> createGenreView(genre, genreIds[i]) }
    }

    @SuppressLint("InflateParams")
    private fun createGenreView(genre: String, genreId: String) {
        val genreTextView = inflater.inflate(R.layout.view_filter_genre, null, false) as FontTextView
        genreTextView.text = genre
        genreTextView.tag = genreId
        genreTextView.isSelected = selectedGenreIdList.contains(genreId)

        genreTextView.setOnClickListener {
            it.isSelected = !it.isSelected

            val clickedGenreId = (it as FontTextView).tag.toString()
            if (selectedGenreIdList.contains(clickedGenreId)) {
                selectedGenreIdList.remove(clickedGenreId)
            } else {
                selectedGenreIdList.add(clickedGenreId)
            }
        }

        val params = FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, itemHeight)
        params.topMargin = itemSpacing
        params.rightMargin = itemSpacing
        addView(genreTextView, params)
    }

    fun setSelectedGenreIds(commaSeparatedGenreIds: String?) {
        if (commaSeparatedGenreIds != null) {
            selectedGenreIdList = ArrayList(commaSeparatedGenreIds.split(","))
        }
    }

    fun getSelectedGenreIds() = selectedGenreIdList.joinToString(",")
}