package com.nuller.popkorn.activities

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.nuller.popkorn.R
import com.nuller.popkorn.adapters.TabsAdapter
import com.nuller.popkorn.fragments.MoviesFragment
import com.nuller.popkorn.reponses.GenreItem
import com.nuller.popkorn.reponses.ResponseGenre
import com.nuller.popkorn.reponses.ResponseGenreItem
import com.nuller.popkorn.utils.Params.Companion.TYPE_MOVIES
import com.nuller.popkorn.utils.Params.Companion.TYPE_SERIES
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllMoviesActivity : BaseActivity() {

    private lateinit var bundle: Bundle
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var myTabsAdapter: TabsAdapter
    private lateinit var textTitle: TextView
    private lateinit var type: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_movies)

        bundle = intent.extras!!
        type = bundle.getString("type")!!
        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)
        textTitle = findViewById(R.id.text_title)

        myTabsAdapter = TabsAdapter(supportFragmentManager)

        when (type) {
            TYPE_MOVIES -> {
                textTitle.text = "فیلم ها"
            }
            TYPE_SERIES -> {
                textTitle.text = "سریال ها"
            }
        }

        getGenres()

    }

    private fun getGenres() {
        myTabsAdapter.clearTabs()
        api.getGenre().enqueue(object : Callback<List<ResponseGenreItem>> {
            override fun onResponse(
                call: Call<List<ResponseGenreItem>>,
                response: Response<List<ResponseGenreItem>>
            ) {
                println(call.request().url())
                val genres = response.body()
                for (genre in genres!!) {
                    val fragment =
                        createFragment("/search/movie/?total_download_link__gt=0&genre=${genre.name}&movie_type=${type}&year__gte=0")
                    myTabsAdapter.addTab(genre.persianName!!, fragment)
                }
                viewPager.adapter = myTabsAdapter
                tabLayout.setupWithViewPager(viewPager)

                changeTabsFont()
            }

            override fun onFailure(call: Call<List<ResponseGenreItem>>, t: Throwable) {
                TODO("Not yet implemented")
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

}