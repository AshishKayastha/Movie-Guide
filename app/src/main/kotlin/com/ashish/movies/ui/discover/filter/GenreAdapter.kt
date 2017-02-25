package com.ashish.movies.ui.discover.filter

import android.content.Context
import android.support.annotation.ArrayRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.ui.widget.FontTextView
import com.ashish.movies.utils.extensions.inflate
import com.ashish.movies.utils.extensions.isNotNullOrEmpty
import java.util.ArrayList

/**
 * Created by Ashish on Jan 25.
 */
class GenreAdapter(context: Context, @ArrayRes genreArrayId: Int, @ArrayRes genreIdArrayId: Int,
                   selectedGenreIds: String? = null) : RecyclerView.Adapter<GenreAdapter.GenreHolder>() {

    private val genres: Array<String>
    private val genreIds: Array<String>
    private var selectedGenreIdList: ArrayList<String>

    init {
        val resources = context.resources
        genres = resources.getStringArray(genreArrayId)
        genreIds = resources.getStringArray(genreIdArrayId)

        if (selectedGenreIds.isNotNullOrEmpty()) {
            selectedGenreIdList = ArrayList(selectedGenreIds!!.split("|"))
        } else {
            selectedGenreIdList = ArrayList<String>()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreHolder {
        return GenreHolder(parent.inflate(R.layout.list_item_filter_genre)!!)
    }

    override fun onBindViewHolder(holder: GenreHolder, position: Int) {
        holder.bindData()
    }

    override fun getItemCount() = genres.size

    fun getSelectedGenreIds() = selectedGenreIdList.joinToString("|")

    inner class GenreHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val genreText: FontTextView by bindView(R.id.genre_text)

        fun bindData() {
            genreText.apply {
                text = genres[adapterPosition]
                val genreId = genreIds[adapterPosition]
                tag = genreId
                isSelected = selectedGenreIdList.contains(genreId)
            }

            genreText.setOnClickListener {
                it.isSelected = !it.isSelected

                val clickedGenreId = it.tag.toString()
                if (selectedGenreIdList.contains(clickedGenreId)) {
                    selectedGenreIdList.remove(clickedGenreId)
                } else {
                    selectedGenreIdList.add(clickedGenreId)
                }
            }
        }
    }
}