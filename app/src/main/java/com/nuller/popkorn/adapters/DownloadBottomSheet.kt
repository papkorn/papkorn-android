package com.nuller.popkorn.adapters

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.DownloadManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import java.util.*

class DownloadBottomSheet(
    private val movieType: String,
    private val imdbId: Int,
    private val api: API
) :
    BottomSheetDialogFragment() {

    private lateinit var linksAdapter: LinksAdapter
    private lateinit var subtitlesAdapter: SubtitlesAdapter
    private lateinit var recyclerLinks: RecyclerView
    private lateinit var recyclerSubtitles: RecyclerView
    private lateinit var loadingEpisodes: ProgressBar
    private lateinit var loadingLinks: ProgressBar
    private lateinit var loadingSubtitles: ProgressBar
    private lateinit var spinnerSeasons: MaterialSpinner
    private lateinit var spinnerEpisodes: MaterialSpinner
    private var seasonsTitles = arrayListOf<String>()
    private var episodesTitles = arrayListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.sheet_download, container, false)
        recyclerLinks = view.findViewById(R.id.recycler_links)
        recyclerSubtitles = view.findViewById(R.id.recycler_subtitles)
        loadingLinks = view.findViewById(R.id.progress_loading_links)
        loadingEpisodes = view.findViewById(R.id.progress_loading_episodes)
        loadingSubtitles = view.findViewById(R.id.progress_loading_subtitles)
        spinnerSeasons = view.findViewById(R.id.spinner_seasons)
        spinnerEpisodes = view.findViewById(R.id.spinner_episodes)

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
//                        println(" فصل ${season.seasonNo}")
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
        loadingEpisodes.visibility = View.VISIBLE
        spinnerEpisodes.visibility = View.INVISIBLE
        spinnerEpisodes.selectedIndex = 0
        episodesTitles.clear()
        api.getSerieEpisodes(imdbId, seasonNumber)
            .enqueue(object : Callback<List<ResponseSerieEpisodesItem>> {
                override fun onResponse(
                    call: Call<List<ResponseSerieEpisodesItem>>,
                    response: Response<List<ResponseSerieEpisodesItem>>
                ) {
                    val episodes = response.body()
                    for (episode in episodes!!) {
                        if (episode.episodeNo != -1 && episode.episodeNo != 0)
                            episodesTitles.add(" قسمت ${episode.episodeNo}")

                        println(" قسمت ${episode.episodeNo}")
                        loadingEpisodes.visibility = View.GONE
                        spinnerEpisodes.visibility = View.VISIBLE

                    }
                    spinnerEpisodes.setItems(episodesTitles)
                    getSerieLinks(
                        imdbId,
                        spinnerSeasons.selectedIndex + 1, 1
                    )
                }

                override fun onFailure(call: Call<List<ResponseSerieEpisodesItem>>, t: Throwable) {

                }

            })
    }

    private fun getMovieLinks(imdb: Int) {
        loadingLinks.visibility = View.VISIBLE
        api.getMovieLinks(imdb)
            .enqueue(object : Callback<List<ResponseMovieLinksItem>> {
                override fun onResponse(
                    call: Call<List<ResponseMovieLinksItem>>,
                    response: Response<List<ResponseMovieLinksItem>>
                ) {
                    if (response.code() == 200) {
                        val data = response.body()
                        linksAdapter = LinksAdapter(data!!,
                            object : OnClick {
                                override fun onClick(position: Int) {
                                    val link = data[position]
                                    startDownload(link.mainUrl + link.uri)
                                }
                            })
                        recyclerLinks.layoutManager = LinearLayoutManager(activity)
                        recyclerLinks.adapter = linksAdapter
                        getMovieSubtitles(imdbId)

                    }
                    loadingLinks.visibility = View.GONE

                }

                override fun onFailure(call: Call<List<ResponseMovieLinksItem>>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun getMovieSubtitles(imdbId: Int) {
        api.getMovieSubtitles(imdbId)
            .enqueue(object : Callback<List<ResponseMovieSubtitlesItem>> {
                override fun onResponse(
                    call: Call<List<ResponseMovieSubtitlesItem>>,
                    response: Response<List<ResponseMovieSubtitlesItem>>
                ) {
                    if (response.code() == 200) {
                        val data = response.body()
                        subtitlesAdapter = SubtitlesAdapter(data!!, object : OnClick {
                            override fun onClick(position: Int) {
                                val link = data[position]
                                startDownload(link.uri)
                            }

                        })
                        recyclerSubtitles.layoutManager = LinearLayoutManager(activity)
                        recyclerSubtitles.adapter = subtitlesAdapter

                    }
                    loadingSubtitles.visibility = View.GONE


                }

                override fun onFailure(call: Call<List<ResponseMovieSubtitlesItem>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun getSerieLinks(imdbId: Int, seasonNumber: Int, episodeNumber: Int) {
        loadingLinks.visibility = View.VISIBLE
        api.getSerieLinks(imdbId, seasonNumber, episodeNumber)
            .enqueue(object : Callback<List<ResponseMovieLinksItem>> {
                override fun onResponse(
                    call: Call<List<ResponseMovieLinksItem>>,
                    response: Response<List<ResponseMovieLinksItem>>
                ) {
                    if (response.code() == 200) {
                        val data = response.body()
                        linksAdapter = LinksAdapter(data!!,
                            object : OnClick {
                                override fun onClick(position: Int) {
                                    val link = data[position]
                                    startDownload(link.mainUrl + link.uri)
                                }
                            })
                        recyclerLinks.layoutManager = LinearLayoutManager(activity)
                        recyclerLinks.adapter = linksAdapter

                        getSerieSubtitles(
                            imdbId,
                            seasonNumber,
                            episodeNumber
                        )

                    }
                    loadingLinks.visibility = View.GONE

                }

                override fun onFailure(call: Call<List<ResponseMovieLinksItem>>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun getSerieSubtitles(imdbId: Int, seasonNumber: Int, episodeNumber: Int) {
        api.getSerieSubtitles(imdbId, seasonNumber, episodeNumber)
            .enqueue(object : Callback<List<ResponseMovieSubtitlesItem>> {
                override fun onResponse(
                    call: Call<List<ResponseMovieSubtitlesItem>>,
                    response: Response<List<ResponseMovieSubtitlesItem>>
                ) {
                    if (response.code() == 200) {
                        val data = response.body()

                        subtitlesAdapter = SubtitlesAdapter(data!!, object : OnClick {
                            override fun onClick(position: Int) {
                                val link = data[position]
                                startDownload(link.uri)
                            }

                        })
                        recyclerSubtitles.layoutManager = LinearLayoutManager(activity)
                        recyclerSubtitles.adapter = subtitlesAdapter

                    }
                    loadingSubtitles.visibility = View.GONE


                }

                override fun onFailure(call: Call<List<ResponseMovieSubtitlesItem>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })

    }


    private fun startDownload(url: String) {
        Toast.makeText(context, "درحال دانلود", Toast.LENGTH_LONG).show()
        var fileName = url.substring(url.lastIndexOf('/') + 1)
        fileName = fileName.substring(0, 1).uppercase(Locale.getDefault()) + fileName.substring(1)
        val request = DownloadManager.Request(Uri.parse(url))
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) // Visibility of the download Notification
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                fileName
            )
            .setTitle(fileName)
            .setDescription(getString(R.string.app_name))
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
        val downloadManager =
            context?.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }

}