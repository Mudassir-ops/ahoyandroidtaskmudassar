package com.example.ahoyandroidtaskmudassar.model.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ahoyandroidtaskmudassar.model.DailyWeatherTable
import com.example.ahoyandroidtaskmudassar.model.MainTable
import kotlinx.coroutines.flow.Flow

@Dao
interface MainDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMainTable(mainTable: MainTable)

    @Query("Select * from main_table")
    fun getAllMainTables(): Flow<List<MainTable>>


    @Query("DELETE FROM main_table")
    suspend fun deleteMainTable()
}

