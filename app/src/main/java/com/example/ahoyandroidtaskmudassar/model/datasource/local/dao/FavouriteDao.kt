package com.example.ahoyandroidtaskmudassar.model.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ahoyandroidtaskmudassar.model.FavouritesTable
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteCity(favouritesTable: FavouritesTable)

    @Query("Select * from favourite_table")
    fun getAllFavouriteCity(): Flow<List<FavouritesTable>>


    @Query("Select * from favourite_table")
    fun getAllFavouriteCityForTesting(): List<FavouritesTable>


    @Query("SELECT * FROM favourite_table where name= :name")
    fun getFavouriteCityByName(name: String): Flow<FavouritesTable>

    @Query("DELETE FROM favourite_table")
    suspend fun deleteFavouriteCityTable()
}

