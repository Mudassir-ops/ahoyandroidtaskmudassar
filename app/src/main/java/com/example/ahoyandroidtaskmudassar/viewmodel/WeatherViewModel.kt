package com.example.ahoyandroidtaskmudassar.viewmodel


import android.util.Log
import androidx.lifecycle.*
import com.example.ahoyandroidtaskmudassar.model.*
import com.example.ahoyandroidtaskmudassar.repository.DatabaseRepository
import com.example.ahoyandroidtaskmudassar.repository.WeatherRepository
import com.example.ahoyandroidtaskmudassar.ui.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class WeatherViewModel
@Inject
constructor(
    private val repository: WeatherRepository,
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    val latData = MutableLiveData<String>()
    val lngData = MutableLiveData<String>()
    val cityName = MutableLiveData<String>()

    val dailyWeatherTableArraylIst = ArrayList<DailyWeatherTable>()

    private val _response2 = MutableLiveData<BaseResponseClassOne>()
    val weatherResponse2: LiveData<BaseResponseClassOne> get() = _response2


    private val _responseCityName = MutableLiveData<BaseResponseClassOne>()
    val weatherResponseByCityName: LiveData<BaseResponseClassOne> get() = _responseCityName

    private val _responseByOneCall = MutableLiveData<ResponseBaseClassSecond>()
    val weatherResponseByOneCall: LiveData<ResponseBaseClassSecond> get() = _responseByOneCall

//    init {
//        getWeather2()
//
//    }

    fun getWeather2() = viewModelScope.launch {
        repository.getWeather2(latData.value!!, lngData.value!!).let { response ->

            if (response.isSuccessful) {
                ///---isnert into db
                databaseRepository.insertMainTable(
                    MainTable(
                        name = response.body()!!.name,
                        feels_like = response.body()!!.mainTable.feels_like,
                        description = response.body()!!.weather2[0].description,
                        temp = response.body()!!.mainTable.temp,
                        temp_max = 00.0, temp_min = 0.0, pressure = 0, humidity = 0)
                )
                _response2.postValue(response.body())
            } else {
                Log.d("tag", "getWeather Error: ${response.code()}")
            }
        }
    }


    fun getWeatherByCityName() = viewModelScope.launch {
        repository.getWeatherByName(cityName.value!!).let { response ->

            if (response.isSuccessful) {
                _responseCityName.postValue(response.body())
            } else {
                Log.d("tag", "getWeather Error: ${response.code()}")
            }
        }
    }

    fun getWeatherByOneCall() = viewModelScope.launch {
        repository.getWeatherByOneCall(latData.value!!, lngData.value!!).let { response ->

            if (response.isSuccessful) {
                dailyWeatherTableArraylIst.clear()
                for (d in response.body()!!.daily) {
                    val dailyWeatherTable = DailyWeatherTable(
                        day = getRIme(d.dt.toLong())!!,
                        temp = "${d.temp.max.toString()}℉",
                        wind_speed = d.wind_speed
                    )
                    dailyWeatherTableArraylIst.add(dailyWeatherTable)
                }

                dailyWeatherTableArraylIst.map { databaseRepository.insertDailyWeather(it) }

                _responseByOneCall.postValue(response.body())
            } else {
                Log.d("tag", "getWeather Error: ${response.code()}")
            }
        }
    }


    val getFavouritesCityWeather: LiveData<List<FavouritesTable>>
        get() =
            databaseRepository.getfavourites.flowOn(Dispatchers.Main)
                .asLiveData(context = viewModelScope.coroutineContext)


    val getdailyWeather: LiveData<List<DailyWeatherTable>>
        get() =
            databaseRepository.getdaily.flowOn(Dispatchers.Main)
                .asLiveData(context = viewModelScope.coroutineContext)


    val getMainTable: LiveData<List<MainTable>>
        get() =
            databaseRepository.getmMain.flowOn(Dispatchers.Main)
                .asLiveData(context = viewModelScope.coroutineContext)



    fun insert(favouritesTable: FavouritesTable) {
        viewModelScope.launch {
            databaseRepository.insert(favouritesTable)
        }
    }

    fun getRIme(timestmap: Long): String? {
        val unixSeconds: Long = timestmap
        val date = Date(unixSeconds * 1000L) // *1000 is to convert seconds to milliseconds
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z") // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-4")) // give a timezone reference for formating (see comment at the bottom
        val formattedDate: String = sdf.format(date)
        // Log.d(MainActivity.TAG, "getRIme: $formattedDate")

        val cal = Calendar.getInstance()
        cal.time = date
        val year = cal[Calendar.YEAR]
        val month = cal[Calendar.MONTH] //here is what you need

        val day = cal[Calendar.DAY_OF_MONTH]
        val day1 = cal[Calendar.DAY_OF_WEEK]

        Log.d(MainActivity.TAG, "getRIme: $day")
        Log.d(MainActivity.TAG, "getRIme: $day1")
        Log.d(MainActivity.TAG, "getRIme: ${getDayName(day1, Locale("en", "DK"))}")

        return getDayName(day1, Locale("en", "DK"))
    }

    fun getDayName(day: Int, locale: Locale?): String? {
        val symbols = DateFormatSymbols(locale)
        val dayNames: Array<String> = symbols.weekdays
        return dayNames[day]
    }
}













