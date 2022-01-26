package com.example.ahoyandroidtaskmudassar


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ahoyandroidtaskmudassar.adapter.FavoruiteCityForcastAdapter
import com.example.ahoyandroidtaskmudassar.adapter.WeeklyWeatherForcastAdapter
import com.example.ahoyandroidtaskmudassar.databinding.ActivityMainBinding
import com.example.ahoyandroidtaskmudassar.model.Daily
import com.example.ahoyandroidtaskmudassar.model.Hourly
import com.example.ahoyandroidtaskmudassar.model.datamodels.clinnic.FavouritesTable
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
import android.R.string.no






@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    var ifSearchCLicked = false
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mSettingsClient: SettingsClient? = null

    val hourlyWeaterData = ArrayList<Hourly>()
    val weekllyWeaterData = ArrayList<Daily>()

    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var mEverydayPendingIntent: PendingIntent

    private lateinit var binding: ActivityMainBinding
    private val viewModel: WeatherViewModel by viewModels()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //----location lat lng of device
        init()
        restoreValuesFromBundle(savedInstanceState)



        val weeklyWeatherForcastAdapter =
            WeeklyWeatherForcastAdapter { Log.d(TAG, "onCreate: $it") }
        val favoruiteCityForcastAdapter =
            FavoruiteCityForcastAdapter { Log.d(TAG, "onCreate: $it") }

        binding.fvBtn.setOnClickListener {
            binding.fvBtn.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_favorite_24, applicationContext.theme))
            viewModel.insert(
                FavouritesTable(
                    name = binding.tvCityName.text.toString(),
                    temp = binding.tvCityTemp.text.toString(),
                    feels_like = binding.tvCityFeelBy.text.toString(),
                    description = binding.tvCityDecription.text.toString()
                )
            )
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


        viewModel.weatherResponse2.observe(this, Observer {
            Log.d("SSsaS", "onCreate: ${it.timezone}")
            //---x numbers weather forcast
            // viewModel.getWeatherByOneCall()
            SharedPreferencesUtil(this as Activity).saveCity(it.name)
            SharedPreferencesUtil(this as Activity).saveTemp("${it.main.temp.toString()}℉  ${it.main.feels_like.toString()}℉")
            binding.apply {

                tvCityName.text = it.name
                tvCityTemp.text = "${it.main.temp} ℉"
                tvCityFeelBy.text = "feels like${it.main.feels_like} ℉"
                tvCityDecription.text = "${it.weather2[0].description}"

            }
            setAlarm()
        })

        viewModel.weatherResponseByOneCall.observe(this, Observer {
//            this.hourlyWeaterData.clear()
//            this.hourlyWeaterData.addAll(it.hourly)
//
//            this.weekllyWeaterData.clear()
//            this.weekllyWeaterData.addAll(it.daily)

            weeklyWeatherForcastAdapter.submitList(null)
            weeklyWeatherForcastAdapter.submitList(it.daily)
            weeklyWeatherForcastAdapter.notifyDataSetChanged()

//            for (h in it.hourly){
//                this.hourlyWeaterData.add(Hourly(h.dt,h.temp,h.feels_like,h.pressure,h.humidity,h.dew_point,h.uvi,h.clouds,h.visibility,h.wind_speed,h.wind_deg,h.wind_gust,h.weather,h.pop))
//
//            }


        })


        binding.btnSearch.setOnClickListener {
            ifSearchCLicked = true
            viewModel.cityName.value = binding.etSearchByCity.text.toString().trim()
            viewModel.getWeatherByCityName()

        }
        viewModel.weatherResponseByCityName.observe(this, Observer {
            Log.d("SSsaS", "onCreate: ${it.timezone}")

            binding.apply {

                tvCityName.text = it.name
                tvCityTemp.text = "${it.main.temp} ℉"
                tvCityFeelBy.text = "feels like${it.main.feels_like} ℉"
                tvCityDecription.text = "${it.weather2[0].description}"
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
            //  binding.tvWind.text= "Last updated on: $mLastUpdateTime"
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
                Toast.makeText(applicationContext, "Started location updates!", Toast.LENGTH_SHORT)
                    .show()
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
        val TAGNUMBER = "TAGNUMBERHERE"
        val TAG: String = "MainActivityTAG"
    }

    private fun setAlarm(){

        val timeNow: Calendar? = Calendar.getInstance()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis();
        calendar[Calendar.HOUR_OF_DAY] = 13
        calendar[Calendar.MINUTE] = 5
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
}