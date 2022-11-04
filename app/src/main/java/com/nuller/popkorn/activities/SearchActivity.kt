package com.nuller.popkorn.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nuller.popkorn.R
import com.nuller.popkorn.adapters.MovieAdapter
import com.nuller.popkorn.adapters.OnBottomReached
import com.nuller.popkorn.adapters.OnClick
import com.nuller.popkorn.reponses.ResponseMovieByUrl
import com.nuller.popkorn.reponses.ResultsItem
import com.nuller.popkorn.utils.Params
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : BaseActivity() {


    private lateinit var searchView: SearchView
    private lateinit var recyclerMovies: RecyclerView
    private lateinit var recyclerSeries: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var serieAdapter: MovieAdapter
    private var movies = arrayListOf<ResultsItem>()
    private var series = arrayListOf<ResultsItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchView = findViewById(R.id.search_view)
        recyclerMovies = findViewById(R.id.recycler_movies)
        recyclerSeries = findViewById(R.id.recycler_series)
        recyclerMovies.layoutManager = GridLayoutManager(context, 2)
        recyclerSeries.layoutManager = GridLayoutManager(context, 2)
        movieAdapter = MovieAdapter(movies)
        serieAdapter = MovieAdapter(series)
        recyclerMovies.adapter = movieAdapter
        recyclerSeries.adapter = serieAdapter

        movieAdapter.onClickListener(object : OnClick {
            override fun onClick(position: Int) {
                val intent = Intent(context, MovieActivity::class.java)
                intent.putExtra("imdb_id", movies[position].imdbId)
                intent.putExtra("type", Params.TYPE_MOVIES)
                startActivity(intent)
            }
        })
        serieAdapter.onClickListener(object : OnClick {
            override fun onClick(position: Int) {
                val intent = Intent(context, MovieActivity::class.java)
                intent.putExtra("imdb_id", movies[position].imdbId)
                intent.putExtra("type", Params.TYPE_SERIES)
                startActivity(intent)
            }
        })

        movieAdapter.onBottomReachListener(object : OnBottomReached {
            override fun onReached() {

            }
        })
        serieAdapter.onBottomReachListener(object : OnBottomReached {
            override fun onReached() {

            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                searchInMovies("movie", p0!!)
                searchInSeries("series", p0)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })

    }

    private fun searchInMovies(movieType: String, search: String) {
        movies.clear()
        api.search(movieType, search)
            .enqueue(object : Callback<ResponseMovieByUrl> {
                override fun onResponse(
                    call: Call<ResponseMovieByUrl>,
                    response: Response<ResponseMovieByUrl>
                ) {
                    println(call.request().url())
                    movies.addAll(response.body()!!.results)
                    movieAdapter.notifyItemInserted(movies.size)
                }

                override fun onFailure(call: Call<ResponseMovieByUrl>, t: Throwable) {
                    println(t.message)
                }

            })


    }

    private fun searchInSeries(movieType: String, search: String) {
        series.clear()
        api.search(movieType, search)
            .enqueue(object : Callback<ResponseMovieByUrl> {
                override fun onResponse(
                    call: Call<ResponseMovieByUrl>,
                    response: Response<ResponseMovieByUrl>
                ) {
                    println(call.request().url())
                    series.addAll(response.body()!!.results)
                    serieAdapter.notifyItemInserted(series.size)
                }

                override fun onFailure(call: Call<ResponseMovieByUrl>, t: Throwable) {
                    println(t.message)
                }

            })


    }
}