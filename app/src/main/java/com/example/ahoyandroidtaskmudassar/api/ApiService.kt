package com.example.ahoyandroidtaskmudassar.api


import com.example.ahoyandroidtaskmudassar.model.BaseResponseClassOne
import com.example.ahoyandroidtaskmudassar.model.ResponseBaseClassSecond
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {


    @GET("weather")
    suspend fun getWeather2(@Query("lat") lat:String,@Query("lon") lon:String,@Query("appid") appid:String,@Query("units") units:String): Response<BaseResponseClassOne>

    @GET("weather")
    suspend fun getWeatherByName(@Query("q") q:String,@Query("appid") appid:String,@Query("units") units:String): Response<BaseResponseClassOne>

    @GET("onecall")
    suspend fun getWeatherByOneCall(@Query("lat") lat:String,@Query("lon") lon:String,@Query("appid") appid:String,@Query("units") units:String): Response<ResponseBaseClassSecond>


}