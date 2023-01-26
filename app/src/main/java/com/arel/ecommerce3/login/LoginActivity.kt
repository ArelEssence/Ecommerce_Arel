package com.arel.ecommerce3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arel.ecommerce3.RegisterViewModel.Companion.TAG
import com.arel.ecommerce3.data.UserApi
import com.arel.ecommerce3.databinding.ActivityLoginBinding
import com.arel.ecommerce3.model.LoginResponseFail
import com.arel.ecommerce3.model.RequestLogin
import com.arel.ecommerce3.model.ResponseLogin
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val emailPattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
    private lateinit var loadingDialog: LoadingDialog
    lateinit var session: SessionLogin
    val _dataLoginError = MutableLiveData<LoginResponseFail>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _toastText = MutableLiveData<Event<String>>()
    val toastText: LiveData<Event<String>> = _toastText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialog = LoadingDialog(this@LoginActivity)


        initAction()
    }

    private fun initAction() {
        session = SessionLogin(applicationContext)

        if (session.isLoggedIn()) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }

        binding.btLogin1.setOnClickListener {
            login()
        }

        binding.btSignup2.setOnClickListener {
            val intentRegister =Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intentRegister)
        }

    }

    private fun login() {

        val email = binding.etEmailLogin1.text.toString()
        val password = binding.etPasswordLogin.text.toString()
        val retrofit = Retrofit().getRetroClientInstance().create(UserApi::class.java)
        var requestLogin = RequestLogin(binding.etEmailLogin1.text.toString().trim(), binding.etPasswordLogin.text.toString().trim())

        when {
            !isValidString(email)-> binding.layoutEmailLogin.error ="Format Email Salah"
            email.isEmpty() && password.isEmpty() -> {
                binding.layoutEmailLogin.error = "email empty"
                binding.layoutPasswordLogin.error = "Password empty"
            }
            else -> {
                showLoadingDialog(true)
            }
        }

        retrofit.login(requestLogin).enqueue(object : Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                if (response.isSuccessful) {

                    //respon logcat
                    val user = response.body()
                    println("response code " + response.code())
                    println("response success " + user)
                    println("response success " + user?.success)
                    Log.e("accessToken", user?.success?.access_token.toString())
                    Log.e("refreshToken", user?.success?.refresh_token.toString())
                    Log.e("email", user?.success?.message.toString())
                    //sampai sini

                    val access_token: String? = user?.success?.access_token.toString()
                    Log.d("check_token_login", access_token.toString())
                    val refresh_token = user?.success?.refresh_token.toString()
                    val id = user?.success?.data_user?.id.toString()
                    val name = user?.success?.data_user?.name.toString()
                    val email = user?.success?.data_user?.email.toString()
                    val phone = user?.success?.data_user?.phone.toString()
                    val gender = user?.success?.data_user?.gender.toString()
                    val image = user?.success?.data_user?.image.toString()

                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    startActivity(intent)
                    showLoadingDialog(false)
                    session.createLoginSession(access_token, refresh_token, id, name, email, phone, gender, image)
                } else {
                    val errorRes = response.errorBody().toString()
                    val errorBody = JSONObject(response.errorBody()?.string()!!).toString()
                    val jsonObject = Gson().fromJson(errorBody, JsonObject::class.java)
                    val error = Gson().fromJson(jsonObject, LoginResponseFail::class.java)
                    _dataLoginError.value = error
                    _toastText.value = Event(error.error?.message.toString())
                    Log.e(TAG, "error : $errorRes")
                    Log.e(TAG, "error : $errorBody")
                    showLoadingDialog(false)

                }
            }
            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Log.e("Error", t.message.toString())
            }
        })
        isLoading.observe(this) {
            showLoadingDialog(it)
        }

        toastText.observe(this) {
            it.getContentIfNotHandled()?.let { toastText ->
                showToast(toastText)
            }
        }
    }


    private fun isValidString(str: String): Boolean {
        return emailPattern.matcher(str).matches()
    }

    private fun showLoadingDialog(isLoading: Boolean) {
        if (isLoading) loadingDialog.startLoading() else loadingDialog.isDismiss()
    }
    private fun showToast(str : String) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }

}