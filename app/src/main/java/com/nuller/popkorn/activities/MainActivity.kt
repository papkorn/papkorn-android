package com.nuller.popkorn.activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.tabs.TabLayout
import com.nuller.popkorn.R
import com.nuller.popkorn.ResponseHomePage
import com.nuller.popkorn.adapters.TabsAdapter
import com.nuller.popkorn.fragments.MoviesFragment
import com.nuller.popkorn.reponses.ResponseAppUpdate
import com.nuller.popkorn.utils.Params.Companion.TYPE_MOVIES
import com.nuller.popkorn.utils.Params.Companion.TYPE_SERIES
import okhttp3.HttpUrl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Url


class MainActivity : BaseActivity(), View.OnClickListener {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var myTabsAdapter: TabsAdapter
    private lateinit var textShowMovies: TextView
    private lateinit var textShowSeries: TextView
    private lateinit var textShowAll: TextView
    private lateinit var buttonSearch: ImageButton
    private var type = TYPE_MOVIES

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)
        textShowMovies = findViewById(R.id.text_show_movies)
        textShowSeries = findViewById(R.id.text_show_series)
        textShowAll = findViewById(R.id.text_show_all)
        buttonSearch = findViewById(R.id.button_search)

        textShowMovies.setOnClickListener(this)
        textShowSeries.setOnClickListener(this)
        textShowAll.setOnClickListener(this)

        myTabsAdapter = TabsAdapter(supportFragmentManager)

        buttonSearch.setOnClickListener {
            startActivity(Intent(context, SearchActivity::class.java))
        }

        getHomePage(type)
        appUpdate()

    }

    private fun appUpdate() {
        api.getAppUpdates()
            .enqueue(object : Callback<ResponseAppUpdate> {
                override fun onResponse(
                    call: Call<ResponseAppUpdate>,
                    response: Response<ResponseAppUpdate>
                ) {
                    val appUpdate = response.body()!!
                    if (appUpdate.isForce!!) {

                        val alert = AlertDialog.Builder(context)
                        alert.setTitle("بروزرسانی!")
                        alert.setMessage("برای استفاده از برنامه لطفا نسخه جدید را نصب نمایید.")
                        alert.setCancelable(false)
                        alert.setPositiveButton(
                            "باشه!"
                        ) { p0, p1 ->
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(appUpdate.downloadLink)
                                )
                            )
                        }
                        alert.show()
                    }

                }

                override fun onFailure(call: Call<ResponseAppUpdate>, t: Throwable) {
                }

            })
    }

    private fun getHomePage(type: String) {
        myTabsAdapter.clearTabs()

        api.getHomePageRows().enqueue(object : Callback<ResponseHomePage> {
            override fun onResponse(
                call: Call<ResponseHomePage>,
                response: Response<ResponseHomePage>
            ) {
                val movies = response.body()?.movie
                val series = response.body()?.serie

                when (type) {
                    TYPE_MOVIES -> {
                        for (movie in movies!!) {
                            val fragment = createFragment(movie?.url!!)
                            myTabsAdapter.addTab(movie.title!!, fragment)
                            println("${movie.title} , ${movie.url}")
                        }
                    }
                    TYPE_SERIES -> {
                        for (movie in series!!) {
                            val fragment = createFragment(movie?.url!!)
                            myTabsAdapter.addTab(movie.title!!, fragment)
                            println("${movie.title} , ${movie.url}")
                        }
                    }
                }

                viewPager.adapter = myTabsAdapter
                tabLayout.setupWithViewPager(viewPager)

                changeTabsFont()

            }

            override fun onFailure(call: Call<ResponseHomePage>, t: Throwable) {
            }

        })
    }

    private fun createFragment(url: String): Fragment {
        val fragment = MoviesFragment()
        val arguments = Bundle()
        arguments.putString("url", url)
        arguments.putString("type", type)
        fragment.arguments = arguments
        return fragment
    }

    private fun changeTabsFont() {
        val vg = tabLayout.getChildAt(0) as ViewGroup
        val tabsCount = vg.childCount
        for (j in 0 until tabsCount) {
            val vgTab = vg.getChildAt(j) as ViewGroup
            val tabChildsCount = vgTab.childCount
            for (i in 0 until tabChildsCount) {
                val tabViewChild = vgTab.getChildAt(i)
                if (tabViewChild is TextView) {
                    tabViewChild.textSize = 12f
                    tabViewChild.typeface = Typeface.createFromAsset(assets, "fonts/iransans.ttf")
                }
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.text_show_movies -> {
                type = TYPE_MOVIES
                textShowMovies.setBackgroundResource(R.drawable.button_primary)
                textShowSeries.setBackgroundResource(android.R.color.transparent)
                textShowAll.text = "همه فیلم‌ها"
                getHomePage(type)
            }
            R.id.text_show_series -> {
                type = TYPE_SERIES
                textShowSeries.setBackgroundResource(R.drawable.button_primary)
                textShowMovies.setBackgroundResource(android.R.color.transparent)
                textShowAll.text = "همه سریال‌ها"
                getHomePage(type)
            }
            R.id.text_show_all -> {
                val intent = Intent(this@MainActivity, AllMoviesActivity::class.java)
                intent.putExtra("type", type)
                startActivity(intent)
            }
        }
    }

}