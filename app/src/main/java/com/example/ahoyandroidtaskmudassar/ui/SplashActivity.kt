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


        viewModel.isInternetAlive.observe(this, Observer {
            if(it){
                Handler().postDelayed({
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                }, 1000)
            }else{
                binding.apply {
                    tvFetching.text="Checking Your Network Connection..."
                }
            }
        })

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun isOnline(): Boolean {

        val connectivityMgr = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val allNetworks: Array<Network> = connectivityMgr.allNetworks // added in API 21 (Lollipop)

        for (network in allNetworks) {
            val networkCapabilities = connectivityMgr.getNetworkCapabilities(network)
            return (networkCapabilities!!.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) &&
                    (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                            || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                            || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)))
        }
        return false
    }
}