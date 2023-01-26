package com.arel.ecommerce3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.arel.ecommerce3.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var preference: SessionLogin
    val fragment = profile()
    companion object {
        val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preference = SessionLogin(this@MainActivity)

        Log.e("check_data", preference.getPreference(SessionLogin.NAME).toString())
        val accessToken = preference.getPreference(SessionLogin.TOKEN)
        Log.d("cek_token", accessToken.toString())

        val navView: BottomNavigationView = binding.navigation

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_fav, R.id.nav_profile
            )
        )
        navView.setupWithNavController(navController)

        //setupWindow()
    }

//    private fun setupWindow() {
//        setSupportActionBar(binding.toolbar)
//        supportActionBar?.setDisplayShowTitleEnabled(false)
//
//
//
//    }
}