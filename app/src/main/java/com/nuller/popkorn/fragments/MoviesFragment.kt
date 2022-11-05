package com.nuller.popkorn.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nuller.popkorn.R
import com.nuller.popkorn.activities.MovieActivity
import com.nuller.popkorn.adapters.MovieAdapter
import com.nuller.popkorn.adapters.OnBottomReached
import com.nuller.popkorn.adapters.OnClick
import com.nuller.popkorn.reponses.ResponseMovieByUrl
import com.nuller.popkorn.reponses.ResultsItem
import com.nuller.popkorn.utils.API
import com.nuller.popkorn.utils.Params
import com.nuller.popkorn.utils.Params.Companion.TYPE_MOVIES
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesFragment() : BaseFragment(), OnClick, OnBottomReached {

    private lateinit var recyclerMovies: RecyclerView
    private lateinit var recyclerAdapter: MovieAdapter
    private var movies = arrayListOf<ResultsItem>()
    private var nextPage = ""
    private var movieType = TYPE_MOVIES

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movies, container, false)
        recyclerAdapter = MovieAdapter(movies, true)
        recyclerMovies = view.findViewById(R.id.recycler_movies)
        recyclerMovies.layoutManager = GridLayoutManager(activity, 2)
        recyclerMovies.adapter = recyclerAdapter
        recyclerAdapter.onClickListener(this)
        recyclerAdapter.onBottomReachListener(this)
        movieType = arguments?.getString("type")!!

        getMoviesByUrl(arguments?.getString("url")!!)

        return view
    }

    private fun getMoviesByUrl(url: String) {
        api.getMoviesByUrl("/api/${url}")
            .enqueue(object : Callback<ResponseMovieByUrl> {
                override fun onResponse(
                    call: Call<ResponseMovieByUrl>,
                    response: Response<ResponseMovieByUrl>
                ) {
                    println("Url-> ${call.request().url()}")

                    val data = response.body()
                    val results = data?.results

                    nextPage = data?.next.toString()
                    movies.addAll(results!!)
                    recyclerAdapter.notifyItemInserted(movies.size)

                }

                override fun onFailure(call: Call<ResponseMovieByUrl>, t: Throwable) {
                    println(t.localizedMessage)
                }

            })

    }

    override fun onClick(position: Int) {
        val intent = Intent(context, MovieActivity::class.java)
        intent.putExtra("imdb_id", movies[position].imdbId)
        intent.putExtra("type", movieType)
        activity?.startActivity(intent)
    }

    override fun onReached() {
        if (nextPage != "null")
            getMoviesByUrl(nextPage.replace(API.BASE_URL, ""))
    }

}