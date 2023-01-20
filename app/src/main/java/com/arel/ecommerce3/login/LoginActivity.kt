package com.arel.ecommerce3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.arel.ecommerce3.data.UserApi
import com.arel.ecommerce3.databinding.ActivityLoginBinding
import com.arel.ecommerce3.model.RequestLogin
import com.arel.ecommerce3.model.ResponseLogin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val emailPattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
    lateinit var session: SessionLogin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                showLoading(true)
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

                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    startActivity(intent)
                    session.createLoginSession(SessionLogin)
                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Log.e("Error", t.message.toString())
            }
        })
    }

    private fun isValidString(str: String): Boolean {
        return emailPattern.matcher(str).matches()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbLogin.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}