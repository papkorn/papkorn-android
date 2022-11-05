package com.nuller.popkorn.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nuller.popkorn.R
import com.nuller.popkorn.reponses.ResultsItem
import com.squareup.picasso.Picasso

class MovieAdapter(private val movies: List<ResultsItem>, private val fullWidthMode: Boolean) :
    RecyclerView.Adapter<MovieAdapter.Holder>() {

    private lateinit var onClick: OnClick
    private lateinit var onBottomReached: OnBottomReached

    class Holder(itemView: View, onClick: OnClick?) : RecyclerView.ViewHolder(itemView) {
        val layoutMovie = itemView.findViewById(R.id.layout_movie) as LinearLayout
        val textMovieName = itemView.findViewById(R.id.text_movie_name) as TextView
        val textMovieIMDB = itemView.findViewById(R.id.text_movie_imdb) as TextView
        val textMovieYear = itemView.findViewById(R.id.text_movie_year) as TextView
        val imageMovieCover = itemView.findViewById(R.id.image_movie_cover) as ImageView

        init {
            itemView.setOnClickListener {
                onClick?.onClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        if (fullWidthMode) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_movie, parent, false)
            return Holder(view, onClick)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_movie2, parent, false)
            return Holder(view, onClick)
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val movie = movies[position]
        holder.textMovieName.text = movie.title
        holder.textMovieIMDB.text = movie.rate.toString()
        holder.textMovieYear.text = movie.year.toString()
        Picasso.get()
            .load(movie.cover.replace("SY1200", "SY300"))
            .into(holder.imageMovieCover)
        if (position == movies.size - 4) {
            onBottomReached.onReached()
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun onClickListener(onClick: OnClick) {
        this.onClick = onClick
    }

    fun onBottomReachListener(onBottomReached: OnBottomReached) {
        this.onBottomReached = onBottomReached
    }
}