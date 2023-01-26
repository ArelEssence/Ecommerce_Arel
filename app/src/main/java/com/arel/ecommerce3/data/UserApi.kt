package com.arel.ecommerce3.data

import com.arel.ecommerce3.ChangePasswordResponse
import com.arel.ecommerce3.RefreshTokenResponse
import com.arel.ecommerce3.RegisterResponse
import com.arel.ecommerce3.model.RequestLogin
import com.arel.ecommerce3.model.ResponseLogin
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserApi {

    @Headers(*["apikey: TuIBt77u7tZHi8n7WqUC"])
    @POST("api/ecommerce/authentication")
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
        @Part("gender") gender: Int,
        @Part image: MultipartBody.Part
    ): Call<RegisterResponse>

    //Refresh Token
    @FormUrlEncoded
    @Headers(*["apikey:TuIBt77u7tZHi8n7WqUC"])
    @POST("training_android/public/api/ecommerce/refresh-token")
    suspend fun refreshToken(
        @Field("id_user") id_user : Int?,
        @Field("access_token") access_token : String?,
        @Field("refresh_token") refresh_token : String?
    ) : Response<RefreshTokenResponse>

    //    @Headers(*["apikey:TuIBt77u7tZHi8n7WqUC"])
    @FormUrlEncoded
    @PUT("api/ecommerce/change-password/{id}")
    fun userChangePassword(
        @Path("id") id : String,
        @Field("password") password : String,
        @Field("new_password") new_Password : String,
        @Field("confirm_password") confirm_password : String,
    ): Call<ChangePasswordResponse>




}