package com.example.ahoyandroidtaskmudassar.api


import com.example.ahoyandroidtaskmudassar.model.Json4Kotlin_Base
import com.example.ahoyandroidtaskmudassar.model.Json4Kotlin_Base2
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {


    @GET("weather")
    suspend fun getWeather2(@Query("lat") lat:String,@Query("lon") lon:String,@Query("appid") appid:String,@Query("units") units:String): Response<Json4Kotlin_Base>

    @GET("weather")
    suspend fun getWeatherByName(@Query("q") q:String,@Query("appid") appid:String,@Query("units") units:String): Response<Json4Kotlin_Base>

    @GET("onecall")
    suspend fun getWeatherByOneCall(@Query("lat") lat:String,@Query("lon") lon:String,@Query("appid") appid:String,@Query("units") units:String): Response<Json4Kotlin_Base2>


}