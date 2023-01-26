package com.arel.ecommerce3

import android.content.Context
import android.content.Intent
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthBadResponse(
    private val preferences: SessionLogin,
    private val context: Context
): Interceptor {

    // untuk refresh token yang kadaluarsa
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code == 401) {
            runBlocking {
                preferences.clear()
            }
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            return response
        }
        return response
    }
}
