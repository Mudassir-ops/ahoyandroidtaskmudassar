package com.example.ahoyandroidtaskmudassar.model.datasource.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ahoyandroidtaskmudassar.model.datasource.local.dao.FavouriteDao
import com.example.ahoyandroidtaskmudassar.model.datamodels.clinnic.FavouritesTable


@Database(entities = [FavouritesTable::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun favouriteDao(): FavouriteDao

}

//@Database(
//    entities = [
//        FavouritesTable::class],
//        version = 1,
//        exportSchema = false
//)
//@TypeConverters(
//    )

//abstract class AppDatabase : RoomDatabase() {
//
//    abstract fun favouriteDao(): FavouriteDao
//
//    companion object {
//
//        @Volatile
//        private var INSTANCE: AppDatabase? = null
//
//        fun getDatabase(context: Context): AppDatabase {
//            val tempInstance = INSTANCE
//            if (tempInstance != null) {
//                return tempInstance
//            }
//            synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDatabase::class.java, "ahoyTask-database"
//                )
//                    .fallbackToDestructiveMigration()
//                    .build()
//                INSTANCE = instance
//                return instance
//            }
//        }
//
//        fun destroyDataBase() {
//            INSTANCE = null
//        }
//    }
//}