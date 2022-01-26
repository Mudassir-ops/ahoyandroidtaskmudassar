package com.example.ahoyandroidtaskmudassar.ui


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ahoyandroidtaskmudassar.adapter.FavoruiteCityForcastAdapter
import com.example.ahoyandroidtaskmudassar.adapter.WeeklyWeatherForcastAdapter
import com.example.ahoyandroidtaskmudassar.databinding.ActivityMainBinding
import com.example.ahoyandroidtaskmudassar.model.Hourly
import com.example.ahoyandroidtaskmudassar.model.FavouritesTable
import com.example.ahoyandroidtaskmudassar.utils.AlarmReceiver
import com.example.ahoyandroidtaskmudassar.utils.SharedPreferencesUtil
import com.example.ahoyandroidtaskmudassar.viewmodel.WeatherViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale
import kotlin.collections.ArrayList
import com.example.ahoyandroidtaskmudassar.BuildConfig
import com.example.ahoyandroidtaskmudassar.R
import com.example.ahoyandroidtaskmudassar.model.DailyWeatherTable


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    var ifSearchCLicked = false
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mSettingsClient: SettingsClient? = null
    private var backPressedOnce = false
    val weekllyWeaterData = ArrayList<DailyWeatherTable>()



    private lateinit var binding: ActivityMainBinding
    private val viewModel: WeatherViewModel by viewModels()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

         if(isOnline()){
             init()
             restoreValuesFromBundle(savedInstanceState)
         }


        val weeklyWeatherForcastAdapter =
            WeeklyWeatherForcastAdapter { Log.d(TAG, "onCreate: $it") }
        val favoruiteCityForcastAdapter =
            FavoruiteCityForcastAdapter {
                Log.d(TAG, "onCreate: $it")
                val intent =Intent(this,WeatherDetailActivity::class.java)
                intent.putExtra("SelectedName",it)
                startActivity(intent)

            }

        binding.fvBtn.setOnClickListener {

                binding.fvBtn.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_favorite_24, applicationContext.theme))
                Toast.makeText(this, "Added to Fav Cities", Toast.LENGTH_SHORT).show()
                viewModel.insert(
                    FavouritesTable(
                        name = binding.tvCityName.text.toString(),
                        temp = binding.tvCityTemp.text.toString(),
                        feels_like = binding.tvCityFeelBy.text.toString(),
                        description = binding.tvCityDecription.text.toString()
                    )
                )


        }

        binding.setting.setOnClickListener {
           // startActivity(Intent(this,SettinActivity::class.java))

        }
        viewModel.getFavouritesCityWeather.observe(this, Observer {


            favoruiteCityForcastAdapter.submitList(null)
            favoruiteCityForcastAdapter.submitList(it as ArrayList<FavouritesTable>)
            favoruiteCityForcastAdapter.notifyDataSetChanged()

        })


        binding.rvWeekly.apply {
            adapter = weeklyWeatherForcastAdapter
            layoutManager = LinearLayoutManager(this@MainActivity).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
        }
        binding.rvFavourtie.apply {
            adapter = favoruiteCityForcastAdapter
            layoutManager = LinearLayoutManager(this@MainActivity).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
        }


        viewModel.getMainTable.observe(this, Observer {

            //---x numbers weather forcast
            // viewModel.getWeatherByOneCall()
            SharedPreferencesUtil(this as Activity).saveCity(it[0].name)
            SharedPreferencesUtil(this as Activity).saveTemp("${it[0].temp}℉  ${it[0].feels_like}℉")
            binding.apply {

                tvCityName.text = it[0].name
                tvCityTemp.text = "${it[0].temp} ℉"
                tvCityFeelBy.text = "feels like${it[0].feels_like} ℉"
                tvCityDecription.text = "${it[0].description}"

            }
            setAlarm()
        })

        viewModel.getdailyWeather.observe(this, Observer {
            weekllyWeaterData.clear()
            weekllyWeaterData.addAll(it)
            if(weekllyWeaterData.size>0){
                weekllyWeaterData.removeAt(0)

                weeklyWeatherForcastAdapter.submitList(null)
                weeklyWeatherForcastAdapter.submitList(weekllyWeaterData)
                weeklyWeatherForcastAdapter.notifyDataSetChanged()
            }



        })


        binding.btnSearch.setOnClickListener {
            if(isOnline()){
                ifSearchCLicked = true
                viewModel.cityName.value = binding.etSearchByCity.text.toString().trim()
                viewModel.getWeatherByCityName()
            }else{
                Toast.makeText(this, "Only Search Work when Internet ALive", Toast.LENGTH_SHORT).show()
            }


        }
        viewModel.weatherResponseByCityName.observe(this, Observer {
            Log.d("SSsaS", "onCreate: ${it.timezone}")

            binding.apply {

                tvCityName.text = it.name
                tvCityTemp.text = "${it.mainTable.temp} ℉"
                tvCityFeelBy.text = "feels like${it.mainTable.feels_like} ℉"
                tvCityDecription.text = it.weather2[0].description
                viewModel.lngData.value = it.coord.lon.toString()
                viewModel.latData.value = it.coord.lat.toString()
                viewModel.getWeatherByOneCall()


            }
        })

    }

    private fun init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mSettingsClient = LocationServices.getSettingsClient(this)
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                mCurrentLocation = locationResult.lastLocation
                mLastUpdateTime = DateFormat.getTimeInstance().format(Date())
                updateLocationUI()
            }
        }
        mRequestingLocationUpdates = false
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest!!.smallestDisplacement = 30.0f
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        mLocationSettingsRequest = builder.build()
    }


    private fun restoreValuesFromBundle(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("is_requesting_updates")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates")
            }
            if (savedInstanceState.containsKey("last_known_location")) {
                mCurrentLocation = savedInstanceState.getParcelable("last_known_location")
            }
            if (savedInstanceState.containsKey("last_updated_on")) {
                mLastUpdateTime = savedInstanceState.getString("last_updated_on")
            }
        }
        updateLocationUI()
    }


    private fun updateLocationUI() {
        if (mCurrentLocation != null) {
            viewModel.latData.value = mCurrentLocation!!.latitude.toString()
            viewModel.lngData.value = mCurrentLocation!!.longitude.toString()
            binding.lastUpdated.text="Last updated on: $mLastUpdateTime"
            viewModel.getWeather2()
            viewModel.getWeatherByOneCall()
        }

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates!!)
        outState.putParcelable("last_known_location", mCurrentLocation)
        outState.putString("last_updated_on", mLastUpdateTime)
    }

    private fun startLocationUpdates() {
        mSettingsClient
            ?.checkLocationSettings(mLocationSettingsRequest)
            ?.addOnSuccessListener(this) {
                Log.i(TAG, "All location settings are satisfied.")
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(this, "Not locations", Toast.LENGTH_SHORT).show()

                }
                mFusedLocationClient!!.requestLocationUpdates(
                    mLocationRequest,
                    mLocationCallback, Looper.myLooper()
                )
                updateLocationUI()
            }
            ?.addOnFailureListener(this) { e ->
                val statusCode = (e as ApiException).statusCode
                when (statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        Log.i(
                            TAG,
                            "Location settings are not satisfied. Attempting to upgrade " +
                                    "location settings "
                        )
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(
                                this,
                                REQUEST_CHECK_SETTINGS
                            )
                        } catch (sie: IntentSender.SendIntentException) {
                            Log.i(TAG, "PendingIntent unable to execute request.")
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        val errorMessage = "Location settings are inadequate, and cannot be " +
                                "fixed here. Fix in Settings."
                        Log.e(TAG, errorMessage)
                        Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
                updateLocationUI()
            }
    }


    fun startLocationButtonClick() {
        Log.d(TAG, "startLocationButtonClick: ")
        //---Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    mRequestingLocationUpdates = true
                    Log.d(TAG, "startLocationButtonClick: ")
                    startLocationUpdates()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    if (response.isPermanentlyDenied) {
                        openSettings()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                RESULT_OK -> Log.e(
                    TAG,
                    "User agreed to make required location settings changes."
                )
                RESULT_CANCELED -> {
                    Log.e(
                        TAG,
                        "User chose not to make required location settings changes."
                    )
                    mRequestingLocationUpdates = false
                }
            }
        }
    }

    private fun openSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts(
            "package",
            BuildConfig.APPLICATION_ID, null
        )
        intent.data = uri
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        if(isOnline()){
            if (!ifSearchCLicked) {
                if (mRequestingLocationUpdates!! && checkPermissions()) {
                    startLocationUpdates()
                } else {
                    startLocationButtonClick()
                    updateLocationUI()
                }
                updateLocationUI()
            }
        }


    }

    private fun checkPermissions(): Boolean {
        val permissionState: Int = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }


    fun getRIme(timestmap: Long) {
        val unixSeconds: Long = 1372339860
        val date = Date(unixSeconds * 1000L) // *1000 is to convert seconds to milliseconds
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z") // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-4")) // give a timezone reference for formating (see comment at the bottom
        val formattedDate: String = sdf.format(date)
        Log.d(TAG, "getRIme: $formattedDate")

        val cal = Calendar.getInstance()
        cal.time = date
        val year = cal[Calendar.YEAR]
        val month = cal[Calendar.MONTH] //here is what you need

        val day = cal[Calendar.DAY_OF_MONTH]
        val day1 = cal[Calendar.DAY_OF_WEEK]

        Log.d(TAG, "getRIme: $day")
        Log.d(TAG, "getRIme: $day1")
        Log.d(TAG, "getRIme: ${getDayName(day1, Locale("en", "DK"))}")


    }

    fun getDayName(day: Int, locale: Locale?): String? {
        val symbols = DateFormatSymbols(locale)
        val dayNames: Array<String> = symbols.weekdays
        return dayNames[day]
    }

    companion object {
        private var mLastUpdateTime: String? = null
        private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 100
        private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 50
        private val REQUEST_CHECK_SETTINGS = 100
        private var mLocationRequest: LocationRequest? = null
        private var mLocationSettingsRequest: LocationSettingsRequest? = null
        private var mLocationCallback: LocationCallback? = null
        private var mCurrentLocation: Location? = null
        private var mRequestingLocationUpdates: Boolean? = null
        val TAG: String = "MainActivityTAG"
    }

    private fun setAlarm(){

        val timeNow: Calendar? = Calendar.getInstance()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis();
        ///---at morning 6 notification
        calendar[Calendar.HOUR_OF_DAY] = 13
        calendar[Calendar.MINUTE] = 12
        calendar[Calendar.SECOND] = 0

        if(calendar.before(timeNow)){
            Log.d(TAG, "added 1 day as time past ")
            calendar.add(Calendar.DATE, 1);
        }else{
            if (calendar.time > Date())calendar.add(Calendar.HOUR_OF_DAY, 0)
            val intent = Intent(applicationContext, AlarmReceiver::class.java)
            intent.putExtra("CITYNAME", SharedPreferencesUtil(this as Activity).getCity())
            intent.putExtra("EVENTID", "0")
            intent.putExtra("TEMP",  SharedPreferencesUtil(this as Activity).getTemp())
            val pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }

    }

    override fun onBackPressed() {


            if (backPressedOnce) {
                super.onBackPressed()
                this.finishAffinity()
            }
            backPressedOnce = true
            Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show()

            Handler().postDelayed({
                backPressedOnce = false
                finish()
            }, 2000)


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