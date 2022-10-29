package com.nuller.popkorn.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nuller.popkorn.R
import com.nuller.popkorn.reponses.ResponseMovieSubtitles
import com.nuller.popkorn.reponses.ResponseMovieSubtitlesItem
import org.w3c.dom.Text

class SubtitlesAdapter(
    private val subtitles: List<ResponseMovieSubtitlesItem>,
    private val onClick: OnClick
) :
    RecyclerView.Adapter<SubtitlesAdapter.Holder>() {

    class Holder(itemView: View, onClick: OnClick) : RecyclerView.ViewHolder(itemView) {
        val textFileName = itemView.findViewById(R.id.text_title) as TextView
        val textQuality = itemView.findViewById(R.id.text_file_quality) as TextView
        val textFileSize = itemView.findViewById(R.id.text_file_size) as TextView

        init {
            itemView.setOnClickListener {
                onClick.onClick(adapterPosition)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_movie_subtitle, parent, false)
        return Holder(view, onClick)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val subtitle = subtitles[position]
        holder.textFileName.text = subtitle.fileName
        holder.textQuality.text = subtitle.author
        holder.textFileSize.text = subtitle.size
    }

    override fun getItemCount(): Int {
        return subtitles.size
    }
}