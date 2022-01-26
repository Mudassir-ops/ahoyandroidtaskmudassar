package com.example.ahoyandroidtaskmudassar.repository

import androidx.annotation.WorkerThread
import com.example.ahoyandroidtaskmudassar.model.datamodels.clinnic.FavouritesTable
import com.example.ahoyandroidtaskmudassar.model.datasource.local.dao.FavouriteDao

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DatabaseRepository @Inject constructor (private val favouriteDao: FavouriteDao) {
    val getfavourites:Flow<List<FavouritesTable>> = favouriteDao.getAllFavouriteCity()

    @WorkerThread
    suspend fun insert(favouritesTable: FavouritesTable) = withContext(Dispatchers.IO){
        favouriteDao.insertFavouriteCity(favouritesTable)
    }
}