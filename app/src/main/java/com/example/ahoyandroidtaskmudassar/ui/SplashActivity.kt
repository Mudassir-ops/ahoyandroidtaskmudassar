package com.example.ahoyandroidtaskmudassar.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import com.example.ahoyandroidtaskmudassar.R
import com.example.ahoyandroidtaskmudassar.databinding.ActivityMainBinding
import com.example.ahoyandroidtaskmudassar.databinding.ActivitySplashBinding
import com.example.ahoyandroidtaskmudassar.viewmodel.SplashViewModel
import com.example.ahoyandroidtaskmudassar.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.setTimeOut(1000)
        viewModel.timeOutDealy.observe(this, Observer {
            Handler().postDelayed({
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            }, it)
        })



    }




}