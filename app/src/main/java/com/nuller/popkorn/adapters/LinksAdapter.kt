package com.nuller.popkorn.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.Hold
import com.nuller.popkorn.R
import com.nuller.popkorn.reponses.ResponseMovieLinksItem

class LinksAdapter(val links: List<ResponseMovieLinksItem>, private val onClick: OnClick) :
    RecyclerView.Adapter<LinksAdapter.Holder>() {


    class Holder(itemView: View, onClick: OnClick) : RecyclerView.ViewHolder(itemView) {

        val cardLinks = itemView.findViewById(R.id.card_links) as CardView
        val textTitle = itemView.findViewById(R.id.text_title) as TextView
        val textFileExt = itemView.findViewById(R.id.text_file_ext) as TextView
        val textFileSize = itemView.findViewById(R.id.text_file_size) as TextView
        val textQuality = itemView.findViewById(R.id.text_file_quality) as TextView
        val layoutIsDubbed = itemView.findViewById(R.id.layout_is_dubbed) as LinearLayout
        val layoutIsSoftSub = itemView.findViewById(R.id.layout_is_soft_sub) as LinearLayout

        init {
            itemView.setOnClickListener {
                onClick.onClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_download_link, parent, false)
        return Holder(view, onClick)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val link = links[position]
        holder.textTitle.text = link.title
        holder.textFileExt.text = link.ext
        holder.textFileSize.text = link.size
        holder.textQuality.text = "انکودر:${link.encoder} ${link.resolution}"
        if (link.isDubbed!!) holder.layoutIsDubbed.visibility = View.VISIBLE
        if (link.isSoftsub!!) holder.layoutIsSoftSub.visibility = View.VISIBLE
    }

    override fun getItemCount(): Int {
        return links.size
    }
}