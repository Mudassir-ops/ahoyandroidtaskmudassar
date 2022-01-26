package com.example.ahoyandroidtaskmudassar.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences


class SharedPreferencesUtil constructor( activity: Activity) {

    private val TEMP_KEY: String = "TEMP_DATA"
    private val CITY_KEY: String = "CITY_DATA"



    private var activity = activity


    fun saveTemp(isSelected: String){
        val editor: SharedPreferences.Editor = activity.getSharedPreferences(TEMP_KEY, Context.MODE_PRIVATE).edit()
        editor.putString("tempretaire", isSelected)
        editor.apply()
    }
    fun getTemp(): String {
        val sharedPref = activity.getSharedPreferences(TEMP_KEY, Context.MODE_PRIVATE)
        return sharedPref.getString("tempretaire", "")!!
    }


    fun saveCity(isSelected: String){
        val editor: SharedPreferences.Editor = activity.getSharedPreferences(CITY_KEY, Context.MODE_PRIVATE).edit()
        editor.putString("city", isSelected)
        editor.apply()
    }
    fun getCity(): String {
        val sharedPref = activity.getSharedPreferences(CITY_KEY, Context.MODE_PRIVATE)
        return sharedPref.getString("city", "")!!
    }


}