package com.example.ahoyandroidtaskmudassar.model.datamodels.clinnic

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

@Entity(tableName = "favourite_table")
data class FavouritesTable(
    @NotNull
    @PrimaryKey
    @SerializedName("id")
    val id: Int?=0,

    @SerializedName("name") val name : String,
    @SerializedName("temp") val temp : String,
    @SerializedName("feels_like") val feels_like : String,
    @SerializedName("description") val description : String,


)
