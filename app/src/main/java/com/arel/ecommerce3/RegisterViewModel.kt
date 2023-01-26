package com.arel.ecommerce3

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arel.ecommerce3.data.UserApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    val dataRegister = MutableLiveData<RegisterResponse>()

    fun setRegister(
        apiKey: String,
        email: RequestBody,
        password: RequestBody,
        name: RequestBody,
        phone: RequestBody,
        gender: Int,
        image: MultipartBody.Part
    ) {
        val retro = Retrofit().getRetroClientInstance().create(UserApi::class.java)
        retro.postRegister(apiKey, email, password, name, phone, gender, image).enqueue(object :
            Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    dataRegister.postValue(response.body())
                    Log.e(TAG, "Success to connect Api to register")
                } else {
                    val errorMessage = response.errorBody().toString()
                    Log.e(TAG, "error : ${errorMessage}")

                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e(TAG, "error ${t.message.toString()}")
                Log.e(TAG, "please, connect your internet")
                println("response error")
            }

        })
    }

    fun getRegister(): LiveData<RegisterResponse> {
        return dataRegister
    }

    companion object {
        const val TAG = "RegisterViewModel"
    }



}