package com.example.ahoyandroidtaskmudassar.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "daily_weather_table")
data class DailyWeatherTable(
    @NonNull
    @PrimaryKey
    @SerializedName("day") val day : String,
    @SerializedName("temp") val temp : String,
    @SerializedName("wind_speed") val wind_speed : Double,
)
