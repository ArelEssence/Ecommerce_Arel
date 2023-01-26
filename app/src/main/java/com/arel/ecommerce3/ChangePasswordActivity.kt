package com.arel.ecommerce3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.arel.ecommerce3.data.ChangePasswordViewModel
import com.arel.ecommerce3.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var changePasswordViewModel: ChangePasswordViewModel
    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var preference: SessionLogin
    val _dataChangePasswordError = MutableLiveData<ChangePasswordResponseFail>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changePasswordViewModel = ViewModelProvider(this)[ChangePasswordViewModel::class.java]
        preference = SessionLogin(this)

        binding.ivBack.setOnClickListener{
        this@ChangePasswordActivity.onBackPressed()
        }

        binding.btSavePassword.setOnClickListener{
            val id = preference.getPreference(SessionLogin.ID)
            val accessToken = preference.getPreference(SessionLogin.TOKEN)
            Log.d("cek_token", accessToken.toString())


            val password = binding.etOldPassword.text.toString()
            val new_password = binding.etNewPassword.text.toString()
            val confirm_new_password = binding.etConfirmPasword.text.toString()

            changePasswordViewModel.setChangePassword(preference, this@ChangePasswordActivity, id.toString(), password, new_password, confirm_new_password)
            changePasswordViewModel.getChangePassword().observe(this@ChangePasswordActivity) {data ->
                val status = data.success?.status
                if (status == 200){
                    Toast.makeText(applicationContext, data.success.message, Toast.LENGTH_SHORT).show()
//                    startActivity(Intent(this, MainActivity::class.java))
                    this@ChangePasswordActivity.onBackPressed()
                }
            }
        }
    }


}