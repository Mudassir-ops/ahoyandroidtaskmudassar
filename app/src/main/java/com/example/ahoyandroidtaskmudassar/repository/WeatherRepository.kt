package com.example.ahoyandroidtaskmudassar.repository

import com.example.ahoyandroidtaskmudassar.api.ApiService
import javax.inject.Inject

class WeatherRepository
@Inject
constructor(private val apiService: ApiService) {

    suspend fun getWeather2(lat:String,lng:String) = apiService.getWeather2(lat,lng,"a344b25fc8164d1b6b126cf8db6d7703","metric")
    suspend fun getWeatherByName(cityName:String) = apiService.getWeatherByName(cityName,"a344b25fc8164d1b6b126cf8db6d7703","metric")
    suspend fun getWeatherByOneCall(lat:String,lng:String) = apiService.getWeatherByOneCall(lat,lng,"a344b25fc8164d1b6b126cf8db6d7703","metric")
}