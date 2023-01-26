package com.arel.ecommerce3

import android.util.Log
import com.arel.ecommerce3.data.UserApi
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthAuthenticator(
    private val preferences: SessionLogin
): Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {


        // fungsi untuk check refresh token & mendapatkan refresh token baru
        return runBlocking {
            val accessToken = preferences.getPreference(SessionLogin.TOKEN)
            val refreshToken = preferences.getPreference(SessionLogin.REFRESH_TOKEN)
            val userId = preferences.getPreference(SessionLogin.ID)

            val newToken = getNewToken(accessToken, refreshToken, userId?.toInt())
            println("token $newToken")

            // clear token
            if (!newToken.isSuccessful || newToken.body() == null || newToken.code() == 401) {
                preferences.clearToken()
            }


            // simpan preference token baru
            newToken.body()?.let {
                val newUserToken = it.success.access_token
                val newRefreshToken = it.success.refresh_token
                preferences.putNewToken(newUserToken, newRefreshToken)
                Log.d("newToken", it.success.access_token)
                Log.d("newRefreshToken", it.success.refresh_token)
                response.request.newBuilder()
                    .header("Authorization", it.success.access_token)
                    .build()
            }
        }
    }

    private suspend fun getNewToken(
        accessToken: String?,
        refreshToken: String?,
        userId: Int?
    ): retrofit2.Response<RefreshTokenResponse> {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(SessionLogin.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        val service = retrofit.create(UserApi::class.java)
        return service.refreshToken(userId, accessToken, refreshToken )
    }
}
