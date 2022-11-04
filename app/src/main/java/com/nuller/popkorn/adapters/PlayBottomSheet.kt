package com.nuller.popkorn.adapters

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.DownloadManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jaredrummler.materialspinner.MaterialSpinner
import com.nuller.popkorn.R
import com.nuller.popkorn.activities.BaseActivity
import com.nuller.popkorn.activities.TestActivity
import com.nuller.popkorn.fragments.BaseFragment
import com.nuller.popkorn.reponses.ResponseMovieLinksItem
import com.nuller.popkorn.reponses.ResponseMovieSubtitlesItem
import com.nuller.popkorn.reponses.ResponseSerieEpisodesItem
import com.nuller.popkorn.reponses.ResponseSerieSeasonsItem
import com.nuller.popkorn.utils.API
import com.nuller.popkorn.utils.Params
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder
import java.util.*

class PlayBottomSheet(
    private val movieType: String,
    private val imdbId: Int,
    private val api: API
) :
    BottomSheetDialogFragment() {

    private lateinit var buttonPlay: Button
    private lateinit var spinnerSeasons: MaterialSpinner
    private lateinit var spinnerEpisodes: MaterialSpinner
    private lateinit var spinnerLinks: MaterialSpinner
    private lateinit var spinnerSubtitles: MaterialSpinner
    private var seasonsTitles = arrayListOf<String>()
    private var episodesTitles = arrayListOf<String>()
    private var linksTitles = arrayListOf<String>()
    private var subtitlesTitles = arrayListOf<String>()
    private var links = arrayListOf<ResponseMovieLinksItem>()
    private var subtitles = arrayListOf<ResponseMovieSubtitlesItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.sheet_play, container, false)
        buttonPlay = view.findViewById(R.id.button_play)
        spinnerSeasons = view.findViewById(R.id.spinner_seasons)
        spinnerEpisodes = view.findViewById(R.id.spinner_episodes)
        spinnerLinks = view.findViewById(R.id.spinner_links)
        spinnerSubtitles = view.findViewById(R.id.spinner_subtitles)

        when (movieType) {
            Params.TYPE_MOVIES -> {
                getMovieLinks(imdbId)
            }
            Params.TYPE_SERIES -> {
                spinnerEpisodes.visibility = View.VISIBLE
                spinnerSeasons.visibility = View.VISIBLE
                getSeasons(imdbId)
            }
        }

        spinnerSeasons.setOnItemSelectedListener { view, position, id, item ->
            getEpisodes(
                imdbId,
                position + 1
            )
        }

        spinnerEpisodes.setOnItemSelectedListener { view, position, id, item ->
            getSerieLinks(
                imdbId,
                spinnerSeasons.selectedIndex + 1,
                position + 1
            )
        }

        buttonPlay.setOnClickListener {
            var videoUrl = ""
            var subtitleUrl = ""
            val videoIntent = Intent(context, TestActivity::class.java)

            if (links.isNotEmpty()) {
                val video = links[spinnerLinks.selectedIndex]
                if (video.isSoftsub!!) {
                    videoUrl = video.mainUrl + video.uri
                } else if (subtitles.isNotEmpty()) {
                    videoUrl = video.mainUrl + video.uri
                    subtitleUrl = subtitles[spinnerSubtitles.selectedIndex].uri
                }else{
                    videoUrl = video.mainUrl + video.uri

                }
            }
            println("videoIntent : $videoUrl")
            println("videoIntent : $subtitleUrl")
            videoIntent.putExtra("video", videoUrl)
            videoIntent.putExtra("subtitle", subtitleUrl)
            startActivity(videoIntent)
        }

        return view

    }

    private fun getSeasons(imdbId: Int) {
        seasonsTitles.clear()
        api.getSerieSeasons(imdbId)
            .enqueue(object : Callback<List<ResponseSerieSeasonsItem>> {
                override fun onResponse(
                    call: Call<List<ResponseSerieSeasonsItem>>,
                    response: Response<List<ResponseSerieSeasonsItem>>
                ) {
                    val seasons = response.body()
                    for (season in seasons!!) {
                        if (season.seasonNo != -1 && season.seasonNo != 0)
                            seasonsTitles.add(" فصل ${season.seasonNo}")
                    }
                    spinnerSeasons.setItems(seasonsTitles)
                    getEpisodes(imdbId, 1)
                }

                override fun onFailure(call: Call<List<ResponseSerieSeasonsItem>>, t: Throwable) {
                }
            })
    }

    private fun getEpisodes(imdbId: Int, seasonNumber: Int) {
        spinnerEpisodes.visibility = View.INVISIBLE
        if (episodesTitles.isNotEmpty()) spinnerEpisodes.selectedIndex = 0
        episodesTitles.clear()
        api.getSerieEpisodes(imdbId, seasonNumber)
            .enqueue(object : Callback<List<ResponseSerieEpisodesItem>> {
                override fun onResponse(
                    call: Call<List<ResponseSerieEpisodesItem>>,
                    response: Response<List<ResponseSerieEpisodesItem>>
                ) {
                    val episodes = response.body()
                    if (response.code() == 200) {
                        for (episode in episodes!!) {
                            if (episode.episodeNo != -1 && episode.episodeNo != 0)
                                episodesTitles.add(" قسمت ${episode.episodeNo}")

                            println(" قسمت ${episode.episodeNo}")
                            spinnerEpisodes.visibility = View.VISIBLE

                        }
                        spinnerEpisodes.setItems(episodesTitles)
                        getSerieLinks(
                            imdbId,
                            spinnerSeasons.selectedIndex + 1, 1
                        )
                    }

                }

                override fun onFailure(call: Call<List<ResponseSerieEpisodesItem>>, t: Throwable) {

                }

            })
    }

    private fun getSerieLinks(imdbId: Int, seasonNumber: Int, episodeNumber: Int) {
        if (linksTitles.isNotEmpty()) spinnerLinks.selectedIndex = 0
        linksTitles.clear()
        links.clear()
        api.getSerieLinks(imdbId, seasonNumber, episodeNumber)
            .enqueue(object : Callback<List<ResponseMovieLinksItem>> {
                override fun onResponse(
                    call: Call<List<ResponseMovieLinksItem>>,
                    response: Response<List<ResponseMovieLinksItem>>
                ) {
                    if (response.code() == 200) {
                        val data = response.body()
                        for (link in data!!) {
                            val str = StringBuilder("${link.resolution!!} ${link.quality}")
                            if (link.encoder != null) {
                                str.append(" ${link.encoder}")
                            }
                            if (link.isDubbed!!) {
                                str.append(" دوبله")
                            }
                            if (link.isSoftsub!!) {
                                str.append(" زیرنویس چسبیده")
                            }
                            linksTitles.add(str.toString())

                        }
                        links.addAll(data)
                        spinnerLinks.setItems(linksTitles)

                        getSerieSubtitles(
                            imdbId,
                            seasonNumber,
                            episodeNumber
                        )

                    }

                }

                override fun onFailure(call: Call<List<ResponseMovieLinksItem>>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun getSerieSubtitles(imdbId: Int, seasonNumber: Int, episodeNumber: Int) {
        if (subtitlesTitles.isNotEmpty()) spinnerSubtitles.selectedIndex = 0
        subtitlesTitles.clear()
        subtitles.clear()
        api.getSerieSubtitles(imdbId, seasonNumber, episodeNumber)
            .enqueue(object : Callback<List<ResponseMovieSubtitlesItem>> {
                override fun onResponse(
                    call: Call<List<ResponseMovieSubtitlesItem>>,
                    response: Response<List<ResponseMovieSubtitlesItem>>
                ) {
                    if (response.code() == 200) {
                        println(call.request().url())
                        val data = response.body()
                        for (subtitle in data!!) {
                            var str =
                                "${subtitle.quality} ${subtitle.resolution} ${subtitle.author}"
//                            if (subtitle.extraInfo.desc != null) str += " ${subtitle.extraInfo.desc}"
                            subtitlesTitles.add(str)
                        }
                        subtitles.addAll(data)
                        spinnerSubtitles.setItems(subtitlesTitles)

                    }


                }

                override fun onFailure(call: Call<List<ResponseMovieSubtitlesItem>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })

    }

    private fun getMovieLinks(imdb: Int) {
        links.clear()
        api.getMovieLinks(imdb)
            .enqueue(object : Callback<List<ResponseMovieLinksItem>> {
                override fun onResponse(
                    call: Call<List<ResponseMovieLinksItem>>,
                    response: Response<List<ResponseMovieLinksItem>>
                ) {
                    if (response.code() == 200) {
                        val data = response.body()
                        for (link in data!!) {
                            val str = StringBuilder("${link.resolution!!} ${link.quality}")
                            if (link.encoder != null) {
                                str.append(" ${link.encoder}")
                            }
                            if (link.isDubbed!!) {
                                str.append(" دوبله")
                            }
                            if (link.isSoftsub!!) {
                                str.append(" زیرنویس چسبیده")
                            }
                            links.addAll(data)
                            linksTitles.add(str.toString())
                            spinnerLinks.setItems(linksTitles)

                        }

                        getMovieSubtitles(imdbId)

                    }
                }

                override fun onFailure(call: Call<List<ResponseMovieLinksItem>>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun getMovieSubtitles(imdbId: Int) {
        subtitles.clear()
        api.getMovieSubtitles(imdbId)
            .enqueue(object : Callback<List<ResponseMovieSubtitlesItem>> {
                override fun onResponse(
                    call: Call<List<ResponseMovieSubtitlesItem>>,
                    response: Response<List<ResponseMovieSubtitlesItem>>
                ) {
                    if (response.code() == 200) {
                        val data = response.body()
                        for (subtitle in data!!) {
                            var str =
                                "${subtitle.quality} ${subtitle.resolution} ${subtitle.author}"
//                            if (subtitle.extraInfo.desc != null) str += " ${subtitle.extraInfo.desc}"
                            subtitlesTitles.add(str)
                        }
                        subtitles.addAll(data)
                        spinnerSubtitles.setItems(subtitlesTitles)

                    }

                }

                override fun onFailure(call: Call<List<ResponseMovieSubtitlesItem>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
    }


}