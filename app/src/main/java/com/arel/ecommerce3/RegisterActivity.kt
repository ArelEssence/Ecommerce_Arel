package com.arel.ecommerce3

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.arel.ecommerce3.databinding.ActivityRegisterBinding
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()
    private val emailPattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
    private val PICK_IMAGE_GALLERY = 1
    private val PICK_IMAGE_CAMERA = 2


    // (Primary Function)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupDataRegister()
        binding.btCamera.setOnClickListener {
            changePhoto()
        }

    }

    // (Secondary Function)
    // setupView Function

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    // setupDataRegister Function

    private fun setupDataRegister() {
        binding.btSignup1.setOnClickListener {
            register()
            // Tampilkan dialog notif "registrasi sukses"
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Registrasi Sukses")
            builder.setMessage("Anda berhasil melakukan registrasi. Silakan login untuk masuk ke aplikasi.")
            builder.setPositiveButton("OK") { _, _ ->
                // Intent ke login activity
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }
            val dialog = builder.create()
            dialog.show()
        }

        binding.btLogin2.setOnClickListener {
            val intentLogin = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intentLogin)
        }

        viewModel.getRegister().observe(this) {
            if (it != null) {
                Toast.makeText(
                    this,
                    "status: ${it.success?.status.toString()} \n${it.success?.message}",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()

            }
            else {
                Toast.makeText(this, "something went wrong \nTry it later", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // changePhoto Function

    private fun changePhoto() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Upload Image")
        dialog.setMessage("Choose source of the image")
        dialog.setPositiveButton("Gallery") { dialog, which ->
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_PICK
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_GALLERY)
        }
        dialog.setNegativeButton("Camera") { dialog, which ->
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, PICK_IMAGE_CAMERA)
        }
        val alert = dialog.create()
        alert.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_GALLERY) {
                val imageUri = data?.data
                binding.uploadPhotoRegister.setImageURI(imageUri)
            } else if (requestCode == PICK_IMAGE_CAMERA) {
                val imageUri = data?.extras?.get("data") as Bitmap
                binding.uploadPhotoRegister.setImageBitmap(imageUri)
            }
        }
    }


    // Eksternal Function

    private fun register() {
        val email = binding.etEmailSignup.text.toString()
        val password = binding.etPasswordSignup.text.toString()
        val name = binding.etNameSignup.text.toString()
        val phone = binding.etPhoneSignup.text.toString()
        val male = binding.rbGenderMale
        val female = binding.rbGenderFemale
        val gender = if (binding.rbGenderFemale.isChecked) isGender(binding.rbGenderFemale.isChecked) else isGender(binding.rbGenderMale.isChecked)
        val imageUri = binding.uploadPhotoRegister

        when {
            !isValidString(email) -> binding.etEmailSignup.error = "Silahkan isi data diri anda"
            email.isEmpty() -> binding.etEmailSignup.error = "Silahkan masukkan email"
            password.isEmpty() -> binding.etEmailSignup.error = "Silahkan masukkan password"
            name.isEmpty() -> binding.etEmailSignup.error = "Silahkan isi nama anda"
            phone.isEmpty() -> binding.etEmailSignup.error = "Silahkan isi nomor telepon"
            !male.isChecked && !female.isChecked -> Toast.makeText(this,"Silahkan pilih gender", Toast.LENGTH_SHORT).show()
            else -> {
                val emailBody = email.toRequestBody("text/plain".toMediaType())
                val passwordBody = password.toRequestBody("text/plain".toMediaType())
                val nameBody = name.toRequestBody("text/plain".toMediaType())
                val phoneBody = phone.toRequestBody("text/plain".toMediaType())

                viewModel.setRegister(
                    apiKey = "TuIBt77u7tZHi8n7WqUC",
                    emailBody,
                    passwordBody,
                    nameBody,
                    phoneBody,
                    gender,
//                    MultipartBody.Part
                )
            }
        }
    }

    private fun isValidString(str: String): Boolean {
        return emailPattern.matcher(str).matches()
    }

    private fun isGender(isChecked: Boolean): Int {
        val female = binding.rbGenderFemale.isChecked
        return if (isChecked == female) {
            1
        } else {
            0
        }
    }

}

