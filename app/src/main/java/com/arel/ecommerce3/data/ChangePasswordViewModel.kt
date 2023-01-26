package com.arel.ecommerce3.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arel.ecommerce3.*
import com.arel.ecommerce3.RegisterViewModel.Companion.TAG
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordViewModel: ViewModel() {
    val changePasswordResponse = MutableLiveData<ChangePasswordResponse>()
    val _dataChangePasswordError = MutableLiveData<ChangePasswordResponseFail>()

    private val _toastText = MutableLiveData<Event<String>>()

    fun setChangePassword(
        preference: SessionLogin,
        context: Context,
        id: String,
        password: String,
        new_password: String,
        confirm_new_password: String,
    ) {
        val retro = ApiConfig().getRetrofitClientInstance(preference, context).create(UserApi::class.java)
        retro.userChangePassword(id, password, new_password, confirm_new_password).enqueue(object :
            Callback<ChangePasswordResponse> {
            override fun onResponse(
                call: Call<ChangePasswordResponse>,
                response: Response<ChangePasswordResponse>
            ) {
                if (response.isSuccessful) {
                    changePasswordResponse.postValue(response.body())
                    _toastText.value = Event(response.body()?.success?.message.toString())
                    Log.e(RegisterViewModel.TAG, "Success to connect Api to register")
                } else

                {
                    val errorRes = response.errorBody().toString()
                    val errorBody = JSONObject(response.errorBody()?.string()).toString()
                    val jsonObject = Gson().fromJson(errorBody, JsonObject::class.java)
                    val error = Gson().fromJson(jsonObject, ChangePasswordResponseFail::class.java)
                    _dataChangePasswordError.value = error
                    _toastText.value = Event(error.error?.message.toString())
//                    _toastText.value = Event(errorBody)
                    Log.e(TAG, "error : ${errorRes}")
                    Log.e(TAG, "error : ${errorBody}")
                }
//                {
//                    val errorMessage = response.errorBody().toString()
//                    Log.e(RegisterViewModel.TAG, "error : ${errorMessage}")
//
//                }
            }

            override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                Log.e(RegisterViewModel.TAG, "error ${t.message.toString()}")
                Log.e(RegisterViewModel.TAG, "please, connect your internet")
                println("response error")
            }

        })
    }
    fun getChangePassword(): LiveData<ChangePasswordResponse> {
        return changePasswordResponse
    }
}