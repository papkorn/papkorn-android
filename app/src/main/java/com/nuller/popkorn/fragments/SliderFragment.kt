package com.nuller.popkorn.fragments

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.nuller.popkorn.R
import com.nuller.popkorn.reponses.ImagesItem
import com.squareup.picasso.Picasso


class SliderFragment : Fragment() {

    private lateinit var imageItem: ImagesItem
    private lateinit var image: ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_slider, container, false)

        image = view.findViewById(R.id.image_slider)
        val imageUrl = arguments?.getString("image_url")
        println(imageUrl)

        Picasso.get()
            .load(imageUrl)
            .into(image)


        return view
    }


}