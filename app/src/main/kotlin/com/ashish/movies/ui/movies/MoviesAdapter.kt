package com.ashish.movies.ui.movies

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ashish.movies.R
import kotlinx.android.synthetic.main.list_item_movie.view.*

/**
 * Created by Ashish on Dec 27.
 */
class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.MoviesHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_movie, parent, false)
        return MoviesHolder(view)
    }

    override fun onBindViewHolder(holder: MoviesHolder, position: Int) {
        holder.itemView.thumbnailImage
    }

    override fun getItemCount(): Int = 0

    fun updateMoviesList() {

    }

    class MoviesHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}