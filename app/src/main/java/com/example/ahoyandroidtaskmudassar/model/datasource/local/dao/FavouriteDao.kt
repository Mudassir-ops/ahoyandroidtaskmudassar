package com.example.ahoyandroidtaskmudassar.model.datasource.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ahoyandroidtaskmudassar.model.datamodels.clinnic.FavouritesTable

@Dao
interface FavouriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteCity(favouritesTable: FavouritesTable)

    @Query("Select * from favourite_table")
    fun getAllFavouriteCity(): LiveData<List<FavouritesTable>>

//    @Query("Select * from favourite_table  where isActive=:isActive")
//    fun getAllIsActiveCinics(isActive: Boolean): LiveData<List<FavouritesTable>>


    @Query("SELECT * FROM favourite_table where name= :name")
    fun getFavouriteCityByName(name: String): LiveData<List<FavouritesTable>>

    @Query("DELETE FROM favourite_table")
    suspend fun deleteFavouriteCityTable()
}

