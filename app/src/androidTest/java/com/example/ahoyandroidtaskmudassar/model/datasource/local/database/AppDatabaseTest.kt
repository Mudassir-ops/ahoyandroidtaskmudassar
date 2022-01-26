package com.example.ahoyandroidtaskmudassar.model.datasource.local.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.ahoyandroidtaskmudassar.model.FavouritesTable
import com.example.ahoyandroidtaskmudassar.model.datasource.local.dao.FavouriteDao
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest : TestCase(){
    private lateinit var favouriteDao: FavouriteDao
    private lateinit var db: AppDatabase

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        favouriteDao = db.favouriteDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeAndReadFavouriteCity() = runBlocking {
       val favouritesTable= FavouritesTable(
            name = "Mudassir",
            temp = "30F",
            feels_like = "40f",
            description = "it is good outisde"
        )
        favouriteDao.insertFavouriteCity(favouritesTable)
        val spends = favouriteDao.getAllFavouriteCityForTesting()
       assertThat(spends.contains(favouritesTable)).isTrue()

    }
}