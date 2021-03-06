package com.example.ahoyandroidtaskmudassar.model
import com.google.gson.annotations.SerializedName

data class BaseResponseClassOne (

	@SerializedName("coord") val coord : Coord,
	@SerializedName("weather") val weather2 : List<Weather2>,
	@SerializedName("base") val base : String,
	@SerializedName("main") val mainTable : MainTable,
	@SerializedName("visibility") val visibility : Int,
	@SerializedName("wind") val wind : Wind,
	@SerializedName("clouds") val clouds : Clouds,
	@SerializedName("dt") val dt : Int,
	@SerializedName("sys") val sys : Sys,
	@SerializedName("timezone") val timezone : Int,
	@SerializedName("id") val id : Int,
	@SerializedName("name") val name : String,
	@SerializedName("cod") val cod : Int,


	)