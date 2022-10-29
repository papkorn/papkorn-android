package com.nuller.popkorn.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.GsonBuilder
import com.nuller.popkorn.utils.API
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class BaseActivity : AppCompatActivity() {

    lateinit var context: Context
    lateinit var retrofit: Retrofit
    lateinit var api: API

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context = this

        retrofit = Retrofit.Builder()
            .baseUrl(API.BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                )
            )
            .build()

        api = retrofit.create(API::class.java)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }
}