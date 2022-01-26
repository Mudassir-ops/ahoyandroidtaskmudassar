package com.example.ahoyandroidtaskmudassar.model.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ahoyandroidtaskmudassar.model.DailyWeatherTable
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyWeather(dailyWeatherTable: DailyWeatherTable)

    @Query("Select * from daily_weather_table")
    fun getAllDailyWeatherList(): Flow<List<DailyWeatherTable>>


    @Query("DELETE FROM daily_weather_table")
    suspend fun deleteDailyWeatherTable()
}

