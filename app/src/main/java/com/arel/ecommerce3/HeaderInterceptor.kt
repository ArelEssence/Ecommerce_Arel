package com.arel.ecommerce3

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(
    private val preferences : SessionLogin
): Interceptor {

    // header untuk hit API setelah login
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            preferences.getPreference(SessionLogin.TOKEN)
        }
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .header("apikey", "TuIBt77u7tZHi8n7WqUC")
            .header("Authorization", token.toString())
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
