package com.arel.ecommerce3

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Retrofit {
    fun getRetroClientInstance(): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        var client = OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl("http://172.17.20.201/training_android/public/api/ecommerce/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}

