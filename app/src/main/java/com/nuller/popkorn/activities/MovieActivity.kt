package com.nuller.popkorn.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.jaredrummler.materialspinner.MaterialSpinner
import com.nuller.popkorn.R
import com.nuller.popkorn.ResponseHomePage
import com.nuller.popkorn.adapters.*
import com.nuller.popkorn.fragments.SliderFragment
import com.nuller.popkorn.reponses.*
import com.nuller.popkorn.utils.Params
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieActivity : BaseActivity() {

    private lateinit var bundle: Bundle
    private var imdbId = -1
    private var movieType = Params.TYPE_SERIES
    private lateinit var viewPager: ViewPager
    private lateinit var tabsAdapter: TabsAdapter
    private lateinit var imageBackDrop: ImageView
    private lateinit var imageCover: ImageView
    private lateinit var textMovieName: TextView
    private lateinit var textMovieYear: TextView
    private lateinit var textMovieDuration: TextView
    private lateinit var textMovieImdb: TextView
    private lateinit var textMoviePlot: TextView
    private lateinit var recyclerSimilar: RecyclerView
    private lateinit var recyclerRecommend: RecyclerView
    private lateinit var buttonDownload: Button
    private lateinit var buttonPlay: Button


    private lateinit var recyclerRecommendAdapter: MovieAdapter
    private lateinit var recyclerSimilarAdapter: MovieAdapter
    private var similarMovies = arrayListOf<ResultsItem>()
    private var recommendMovies = arrayListOf<ResultsItem>()


    private lateinit var progressLoading: ProgressBar
    private lateinit var layoutMovie: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        checkPermission()

        bundle = intent.extras!!
        imdbId = bundle.getInt("imdb_id")
        movieType = bundle.getString("type")!!
        viewPager = findViewById(R.id.view_pager)
        imageBackDrop = findViewById(R.id.image_movie_back_drop)
        imageCover = findViewById(R.id.image_movie_cover)
        textMovieName = findViewById(R.id.text_movie_name)
        textMovieYear = findViewById(R.id.text_movie_year)
        textMovieDuration = findViewById(R.id.text_movie_duration)
        textMovieImdb = findViewById(R.id.text_movie_imdb)
        textMoviePlot = findViewById(R.id.text_movie_plot)
        recyclerSimilar = findViewById(R.id.recycler_movie_similar)
        recyclerRecommend = findViewById(R.id.recycler_movie_recommend)
        buttonDownload = findViewById(R.id.button_download)
        buttonPlay = findViewById(R.id.button_play)

        buttonDownload.setOnClickListener {
            val downloadBottomSheet = DownloadBottomSheet(movieType, imdbId, api)
            downloadBottomSheet.show(supportFragmentManager, "downloadBottomSheet")

        }
        buttonPlay.setOnClickListener {
            val playBottomSheet = PlayBottomSheet(movieType, imdbId, api)
            playBottomSheet.show(supportFragmentManager, "playBottomSheet")

        }

        progressLoading = findViewById(R.id.progress_loading)
        layoutMovie = findViewById(R.id.layout_movie)

        tabsAdapter = TabsAdapter(supportFragmentManager)
        recyclerSimilarAdapter = MovieAdapter(similarMovies,false)
        recyclerRecommendAdapter = MovieAdapter(recommendMovies,false)
        recyclerSimilar.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerRecommend.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerSimilar.adapter = recyclerSimilarAdapter
        recyclerRecommend.adapter = recyclerRecommendAdapter


        getMovie(imdbId)
        getMovieImages(imdbId, 5)
        getSimilarMovies(imdbId, 12)
        getRecommendMovies(imdbId, 12)

        recyclerRecommendAdapter.onClickListener(object : OnClick {
            override fun onClick(position: Int) {
                val intent = Intent(context, MovieActivity::class.java)
                intent.putExtra("imdb_id", recommendMovies[position].imdbId)
                intent.putExtra("type", movieType)

//                if (recommendMovies[position].movieType == 1) intent.putExtra(
//                    "type",
//                    Params.TYPE_MOVIES
//                )
//                else intent.putExtra("type", Params.TYPE_SERIES)

                startActivity(intent)
            }
        })
        recyclerSimilarAdapter.onClickListener(object : OnClick {
            override fun onClick(position: Int) {
                val intent = Intent(context, MovieActivity::class.java)
                intent.putExtra("imdb_id", similarMovies[position].imdbId)
                intent.putExtra("type", movieType)
//                if (similarMovies[position].movieType == 1) intent.putExtra(
//                    "type",
//                    Params.TYPE_MOVIES
//                )
//                else intent.putExtra("type", Params.TYPE_SERIES)

                startActivity(intent)
            }
        })
        recyclerRecommendAdapter.onBottomReachListener(object : OnBottomReached {
            override fun onReached() {
            }
        })
        recyclerSimilarAdapter.onBottomReachListener(object : OnBottomReached {
            override fun onReached() {
            }
        })

    }

    private fun getMovie(imdbId: Int) {
        api.getMovie(imdbId)
            .enqueue(object : Callback<ResponseMovie> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<ResponseMovie>,
                    response: Response<ResponseMovie>
                ) {
                    val movie = response.body()
                    textMovieName.text = movie?.title
                    textMovieYear.text = "${movie?.year}"
                    textMovieDuration.text = "${movie?.duration} min"
                    textMovieImdb.text = "${movie?.rate}"
                    textMoviePlot.text = movie?.plot

                    Picasso.get()
                        .load(movie?.backdrop)
                        .into(imageBackDrop)
                    Picasso.get()
                        .load(movie?.cover?.replace("SY1200", "SY300"))
                        .into(imageCover)

                    layoutMovie.visibility = View.VISIBLE
                    progressLoading.visibility = View.GONE

                }

                override fun onFailure(call: Call<ResponseMovie>, t: Throwable) {
                    println(t.message)
                    println(call.request().url())
                }

            })

    }

    private fun getMovieImages(imdbId: Int, limit: Int) {
        api.getMovieImages(imdbId, limit)
            .enqueue(object : Callback<ResponseMovieImages> {
                override fun onResponse(
                    call: Call<ResponseMovieImages>,
                    response: Response<ResponseMovieImages>
                ) {
                    if (response.code() == 200) {
                        val data = response.body()
                        val images = data?.images


                        for (image in images!!) {
                            println(image?.filePath)
                            val sliderFragment = SliderFragment()
                            val arguments = Bundle()
                            arguments.putString(
                                "image_url",
                                "${data.w500}${image?.filePath}"
                            )
                            sliderFragment.arguments = arguments
                            tabsAdapter.addTab("", sliderFragment)
                        }

                        viewPager.adapter = tabsAdapter
                    }


                }

                override fun onFailure(call: Call<ResponseMovieImages>, t: Throwable) {

                }

            })
    }

    private fun getSimilarMovies(imdbId: Int, count: Int) {
        api.getSimilarMovies(imdbId, count)
            .enqueue(object : Callback<ResponseMovieByUrl> {
                override fun onResponse(
                    call: Call<ResponseMovieByUrl>,
                    response: Response<ResponseMovieByUrl>
                ) {
                    val data = response.body()
                    val movies = data?.results!!
                    similarMovies.addAll(movies)
                    recyclerSimilarAdapter.notifyItemInserted(movies.size)
                }

                override fun onFailure(call: Call<ResponseMovieByUrl>, t: Throwable) {
                    println(t.message)
                }

            })

    }

    private fun getRecommendMovies(imdbId: Int, count: Int) {
        api.getRecommendMovies(imdbId, count)
            .enqueue(object : Callback<ResponseMovieByUrl> {
                override fun onResponse(
                    call: Call<ResponseMovieByUrl>,
                    response: Response<ResponseMovieByUrl>
                ) {
                    val data = response.body()
                    val movies = data?.results!!
                    recommendMovies.addAll(movies)
                    recyclerRecommendAdapter.notifyItemInserted(movies.size)
                }

                override fun onFailure(call: Call<ResponseMovieByUrl>, t: Throwable) {
                    println(t.message)
                }

            })

    }


    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) ==
            PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1001
            )
        }
    }
}