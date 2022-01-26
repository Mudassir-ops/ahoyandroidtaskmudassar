package com.example.ahoyandroidtaskmudassar.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "main_table")
data class MainTable (
	@NonNull
	@PrimaryKey
	@SerializedName("name") val name : String,
	@SerializedName("temp") val temp : Double,
	@SerializedName("feels_like") val feels_like : Double,
	@SerializedName("temp_min") val temp_min : Double,
	@SerializedName("temp_max") val temp_max : Double,
	@SerializedName("pressure") val pressure : Int,
	@SerializedName("humidity") val humidity : Int,
	@SerializedName("description") val description : String,
)