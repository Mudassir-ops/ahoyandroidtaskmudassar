package com.example.ahoyandroidtaskmudassar.model.datasource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ahoyandroidtaskmudassar.model.datasource.local.dao.FavouriteDao
import com.example.ahoyandroidtaskmudassar.model.FavouritesTable
import com.example.ahoyandroidtaskmudassar.model.DailyWeatherTable
import com.example.ahoyandroidtaskmudassar.model.MainTable
import com.example.ahoyandroidtaskmudassar.model.datasource.local.dao.DailyWeatherDao
import com.example.ahoyandroidtaskmudassar.model.datasource.local.dao.MainDao


@Database(entities = [FavouritesTable::class, DailyWeatherTable::class,MainTable::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun favouriteDao(): FavouriteDao
    abstract fun dailyweatherDao(): DailyWeatherDao
    abstract fun mainDao(): MainDao

}