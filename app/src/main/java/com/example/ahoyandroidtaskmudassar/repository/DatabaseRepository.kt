package com.example.ahoyandroidtaskmudassar.repository

import androidx.annotation.WorkerThread
import com.example.ahoyandroidtaskmudassar.model.FavouritesTable
import com.example.ahoyandroidtaskmudassar.model.DailyWeatherTable
import com.example.ahoyandroidtaskmudassar.model.MainTable
import com.example.ahoyandroidtaskmudassar.model.datasource.local.dao.DailyWeatherDao
import com.example.ahoyandroidtaskmudassar.model.datasource.local.dao.FavouriteDao
import com.example.ahoyandroidtaskmudassar.model.datasource.local.dao.MainDao

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DatabaseRepository @Inject constructor (private val favouriteDao: FavouriteDao,private val dailyWeatherDao: DailyWeatherDao,val mainDao: MainDao) {

    val getfavourites:Flow<List<FavouritesTable>> = favouriteDao.getAllFavouriteCity()
    val getdaily:Flow<List<DailyWeatherTable>> = dailyWeatherDao.getAllDailyWeatherList()
    val getmMain:Flow<List<MainTable>> = mainDao.getAllMainTables()

    //val getFavouriteCityByName:Flow<List<FavouritesTable>> = favouriteDao.getFavouriteCityByName()

     fun getFavouriteCityByName(name:String)=favouriteDao.getFavouriteCityByName(name)

    @WorkerThread
    suspend fun insert(favouritesTable: FavouritesTable) = withContext(Dispatchers.IO){
        favouriteDao.insertFavouriteCity(favouritesTable)
    }


    @WorkerThread
    suspend fun insertDailyWeather(dailyWeatherTable: DailyWeatherTable) = withContext(Dispatchers.IO){
        dailyWeatherDao.insertDailyWeather(dailyWeatherTable)
    }


    @WorkerThread
    suspend fun insertMainTable(mainTable: MainTable) = withContext(Dispatchers.IO){
        mainDao.insertMainTable(mainTable)
    }

}