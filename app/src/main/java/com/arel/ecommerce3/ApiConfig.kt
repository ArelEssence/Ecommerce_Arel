package com.arel.ecommerce3

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiConfig {
//    private const val BASE_URL = "http://172.17.20.238/training_android/public/"

    fun getRetrofitClientInstance(pref: SessionLogin, context: Context): Retrofit {

        val gson = GsonBuilder().setLenient().create()
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(HeaderInterceptor(pref)) //header
            .addInterceptor(AuthBadResponse(pref, context)) // 401 bad response
            .authenticator(AuthAuthenticator(pref)) // get refresh token
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl(SessionLogin.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }
}