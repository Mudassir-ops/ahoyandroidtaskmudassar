package com.example.ahoyandroidtaskmudassar.viewmodel



import android.util.Log
import androidx.lifecycle.*
import com.example.ahoyandroidtaskmudassar.model.Json4Kotlin_Base
import com.example.ahoyandroidtaskmudassar.model.Json4Kotlin_Base2
import com.example.ahoyandroidtaskmudassar.model.datamodels.clinnic.FavouritesTable
import com.example.ahoyandroidtaskmudassar.repository.DatabaseRepository
import com.example.ahoyandroidtaskmudassar.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel
@Inject
constructor(private val repository: WeatherRepository,private val databaseRepository: DatabaseRepository) : ViewModel() {

    val latData=MutableLiveData<String>()
    val lngData=MutableLiveData<String>()
    val cityName=MutableLiveData<String>()

    private val _response2 = MutableLiveData<Json4Kotlin_Base>()
    val weatherResponse2: LiveData<Json4Kotlin_Base> get() = _response2


    private val _responseCityName = MutableLiveData<Json4Kotlin_Base>()
    val weatherResponseByCityName: LiveData<Json4Kotlin_Base> get() = _responseCityName

    private val _responseByOneCall = MutableLiveData<Json4Kotlin_Base2>()
    val weatherResponseByOneCall: LiveData<Json4Kotlin_Base2> get() = _responseByOneCall

//    init {
//        getWeather2()
//
//    }

     fun getWeather2() = viewModelScope.launch {
        repository.getWeather2(latData.value!!,lngData.value!!).let { response ->

            if (response.isSuccessful) {
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
        repository.getWeatherByOneCall(latData.value!!,lngData.value!!).let { response ->

            if (response.isSuccessful) {
                _responseByOneCall.postValue(response.body())
            } else {
                Log.d("tag", "getWeather Error: ${response.code()}")
            }
        }
    }


    val getFavouritesCityWeather:LiveData<List<FavouritesTable>> get() =
        databaseRepository.getfavourites.flowOn(Dispatchers.Main).asLiveData(context = viewModelScope.coroutineContext)

    fun insert(favouritesTable: FavouritesTable){
        viewModelScope.launch {
            databaseRepository.insert(favouritesTable)
        }
    }
}













