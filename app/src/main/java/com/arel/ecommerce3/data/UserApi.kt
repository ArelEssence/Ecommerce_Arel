package com.arel.ecommerce3.data

import com.arel.ecommerce3.RegisterResponse
import com.arel.ecommerce3.model.RequestLogin
import com.arel.ecommerce3.model.ResponseLogin
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface UserApi {

    @Headers(*["apikey: TuIBt77u7tZHi8n7WqUC"])
    @POST("authentication")
    fun login(
        @Body userRequest: RequestLogin
    ): Call<ResponseLogin>

    @Multipart
    @POST("api/ecommerce/registration")
    fun postRegister(
        @Header("apikey") apikey: String,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("name") name: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("gender") gender: Int
    ): Call<RegisterResponse>

}