package com.arel.ecommerce3

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arel.ecommerce3.databinding.ActivityRegisterBinding
import com.arel.ecommerce3.utils.createCustomTempFile
import com.arel.ecommerce3.utils.rotateBitmap
import com.arel.ecommerce3.utils.uriToFile
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var loadingDialog: LoadingDialog
    private val viewModel by viewModels<RegisterViewModel>()
    private val emailPattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
    private val PICK_IMAGE_GALLERY = 1
    private val PICK_IMAGE_CAMERA = 2
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _toastText = MutableLiveData<Event<String>>()
    val toastText: LiveData<Event<String>> = _toastText

    companion object {
        val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {

            if (!allPermissionGranted()) {
                Toast.makeText(this, "Tidak mendapatkan permission", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    // (Primary Function)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDataRegister()
        binding.btCamera.setOnClickListener {
            changePhoto()
        }

        if (!allPermissionGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

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
            startTakePhoto()
        }
        val alert = dialog.create()
        alert.show()
    }

    private var getFile: File? = null
    private lateinit var currentPhotoPath: String

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_GALLERY) {
                val imageUri = data?.data as Uri
                val myFile = uriToFile(imageUri, this@RegisterActivity)
                getFile = myFile
                binding.uploadPhotoRegister.setImageURI(imageUri)
            }
        }
    }
        private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        createCustomTempFile(application).also {
            val photoURI : Uri = FileProvider.getUriForFile(
                this@RegisterActivity,
                "com.arel.ecommerce3",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }

    }
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            val isBackCamera = true

            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )
//            val result = BitmapFactory.decodeFile(myFile.path)
            binding.uploadPhotoRegister.setImageBitmap(result)
        }
    }




    // Eksternal Function

    private fun register() {
        val file = reduceFileImage(getFile as File)
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image",
            file.name,
            requestImageFile)
        val email = binding.etEmailSignup.text.toString()
        val password = binding.etPasswordSignup.text.toString()
        val name = binding.etNameSignup.text.toString()
        val phone = binding.etPhoneSignup.text.toString()
        val male = binding.rbGenderMale
        val female = binding.rbGenderFemale
        val gender = if (binding.rbGenderFemale.isChecked) isGender(binding.rbGenderFemale.isChecked) else isGender(binding.rbGenderMale.isChecked)


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
                    imageMultiPart
                )
            }
        }
        isLoading.observe(this) {
            showLoadingDialog(it)
        }

        toastText.observe(this) {
            it.getContentIfNotHandled()?.let { toastText ->
                showToast(toastText)
            }
        }
    }


    private fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmPicByArray = bmpStream.toByteArray()
            streamLength = bmPicByArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
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

    private fun showLoadingDialog(isLoading: Boolean) {
        if (isLoading) loadingDialog.startLoading() else loadingDialog.isDismiss()
    }
    private fun showToast(str : String) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }

}

