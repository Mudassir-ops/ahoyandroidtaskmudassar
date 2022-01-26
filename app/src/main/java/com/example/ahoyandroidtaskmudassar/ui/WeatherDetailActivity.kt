package com.example.ahoyandroidtaskmudassar.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.ahoyandroidtaskmudassar.R
import com.example.ahoyandroidtaskmudassar.databinding.ActivityMainBinding
import com.example.ahoyandroidtaskmudassar.databinding.ActivityWeatherDetailBinding
import com.example.ahoyandroidtaskmudassar.viewmodel.SplashViewModel
import com.example.ahoyandroidtaskmudassar.viewmodel.WeatherDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class WeatherDetailActivity : AppCompatActivity() {

    companion object{
        val TAG="WeatherDetailActiTAG"
    }
    private val viewModel: WeatherDetailViewModel by viewModels()
    private lateinit var binding: ActivityWeatherDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ss:String = intent.getStringExtra("SelectedName").toString()
       viewModel.name.value=ss

        viewModel.data.observe(this, Observer {
            binding.apply {
                cityName.text=it.name
                temp.text=it.temp
                humidit.text=it.description
                feelsLike.text=it.feels_like
            }
            Log.d(TAG, "onCreate: $it ")
        })


    }
}