package com.example.ahoyandroidtaskmudassar.di

import android.content.Context
import androidx.room.Room
import com.example.ahoyandroidtaskmudassar.model.datasource.local.dao.DailyWeatherDao
import com.example.ahoyandroidtaskmudassar.model.datasource.local.dao.FavouriteDao
import com.example.ahoyandroidtaskmudassar.model.datasource.local.dao.MainDao
import com.example.ahoyandroidtaskmudassar.model.datasource.local.database.AppDatabase
import com.example.ahoyandroidtaskmudassar.repository.DatabaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModuleDatabase  {

    @Provides
    fun providesUserDao(userDatabase: AppDatabase):FavouriteDao = userDatabase.favouriteDao()

    @Provides
    fun dailyWeatherDao(userDatabase: AppDatabase): DailyWeatherDao = userDatabase.dailyweatherDao()

    @Provides
    fun mainDao(userDatabase: AppDatabase): MainDao = userDatabase.mainDao()

//    @Provides
//    fun provides(userDatabase: AppDatabase):FavouriteDao = userDatabase.da()

    @Provides
    @Singleton
    fun providesUserDatabase(@ApplicationContext context: Context):AppDatabase
            = Room.databaseBuilder(context,AppDatabase::class.java,"UserDatabase").build()


}