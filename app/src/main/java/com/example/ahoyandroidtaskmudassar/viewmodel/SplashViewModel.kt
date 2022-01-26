package com.example.ahoyandroidtaskmudassar.viewmodel



import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.*
import com.example.ahoyandroidtaskmudassar.MyApplication
import com.example.ahoyandroidtaskmudassar.model.BaseResponseClassOne
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
@Inject
constructor() : ViewModel() {

    private val timeOut = MutableLiveData<Long>()
     val isInternetAlive = MutableLiveData<Boolean>()
    val timeOutDealy: LiveData<Long> get() = timeOut

    init {
       isInternetAlive.value= isOnline(MyApplication.getInstance())
    }

     fun getTimeOut(splashTimeOut: Long) = viewModelScope.launch {
        timeOut.value=splashTimeOut
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun isOnline(context: Context): Boolean {

        val connectivityMgr = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
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













