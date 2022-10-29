package com.nuller.popkorn.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import com.nuller.popkorn.utils.API
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class BaseFragment: Fragment() {

    lateinit var retrofit: Retrofit
    lateinit var api: API


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
    }
}