package com.ashish.movieguide.ui.discover.filter

import android.content.Context
import android.support.annotation.ArrayRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.ashish.movieguide.R
import com.ashish.movieguide.ui.widget.FontTextView
import com.ashish.movieguide.utils.extensions.addOrRemove
import com.ashish.movieguide.utils.extensions.bindView
import com.ashish.movieguide.utils.extensions.inflate
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import java.util.ArrayList

/**
 * Created by Ashish on Jan 25.
 */
class GenreAdapter(
        context: Context,
        @ArrayRes genreArrayId: Int,
        @ArrayRes genreIdArrayId: Int,
        selectedGenreIds: String? = null
) : RecyclerView.Adapter<GenreAdapter.GenreHolder>() {

    companion object {
        private const val SEPARATOR = "|"
    }

    private val genres: Array<String>
    private val genreIds: Array<String>
    private var selectedGenreIdList: ArrayList<String>

    init {
        val resources = context.resources
        genres = resources.getStringArray(genreArrayId)
        genreIds = resources.getStringArray(genreIdArrayId)

        selectedGenreIdList = if (selectedGenreIds.isNotNullOrEmpty()) {
            ArrayList(selectedGenreIds!!.split(SEPARATOR))
        } else {
            ArrayList()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreHolder {
        return GenreHolder(parent.inflate(R.layout.list_item_filter_genre)!!)
    }

    override fun onBindViewHolder(holder: GenreHolder, position: Int) {
        holder.bindData()
    }

    override fun getItemCount() = genres.size

    fun getSelectedGenreIds() = selectedGenreIdList.joinToString(SEPARATOR)

    inner class GenreHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val genreText: FontTextView by bindView(R.id.genre_text)

        fun bindData() {
            genreText.run {
                text = genres[adapterPosition]
                val genreId = genreIds[adapterPosition]
                tag = genreId
                isSelected = selectedGenreIdList.contains(genreId)
            }

            genreText.setOnClickListener { view ->
                view.isSelected = !view.isSelected
                val clickedGenreId = view.tag.toString()
                selectedGenreIdList.addOrRemove(clickedGenreId)
            }
        }
    }
}