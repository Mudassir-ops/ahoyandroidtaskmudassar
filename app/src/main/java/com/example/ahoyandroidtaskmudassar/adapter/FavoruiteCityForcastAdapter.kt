package com.example.ahoyandroidtaskmudassar.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ahoyandroidtaskmudassar.ui.MainActivity
import com.example.ahoyandroidtaskmudassar.databinding.ForcastLayoutBinding
import com.example.ahoyandroidtaskmudassar.model.datamodels.clinnic.FavouritesTable
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*


class FavoruiteCityForcastAdapter(private val callback: (Int) -> Unit) : ListAdapter<FavouritesTable, FavoruiteCityForcastAdapter.ViewHolder>(DiffCallbak()){

    class ViewHolder(private val binding: ForcastLayoutBinding, val callback: (Int) -> Unit): RecyclerView.ViewHolder(binding.root) {

        fun bind(favouritesTable: FavouritesTable){
            binding.apply {

                tvTime.text=favouritesTable.name
                tvTmep.text = favouritesTable.temp
                tvWind.text = favouritesTable.description

            }
        }

        fun getRIme(timestmap:Long): String? {
            val unixSeconds: Long = timestmap
            val date = Date(unixSeconds * 1000L) // *1000 is to convert seconds to milliseconds
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z") // the format of your date
            sdf.setTimeZone(TimeZone.getTimeZone("GMT-4")) // give a timezone reference for formating (see comment at the bottom
            val formattedDate: String = sdf.format(date)
            // Log.d(MainActivity.TAG, "getRIme: $formattedDate")

            val cal = Calendar.getInstance()
            cal.time = date
            val year = cal[Calendar.YEAR]
            val month = cal[Calendar.MONTH] //here is what you need

            val day = cal[Calendar.DAY_OF_MONTH]
            val day1 = cal[Calendar.DAY_OF_WEEK]

            Log.d(MainActivity.TAG, "getRIme: $day")
            Log.d(MainActivity.TAG, "getRIme: $day1")
            Log.d(MainActivity.TAG, "getRIme: ${getDayName(day1,  Locale("en", "DK") )}")

            return getDayName(day1,  Locale("en", "DK") )
        }

        fun getDayName(day: Int, locale: Locale?): String? {
            val symbols = DateFormatSymbols(locale)
            val dayNames: Array<String> = symbols.weekdays
            return dayNames[day]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ForcastLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v, callback)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentLeague = getItem(position)
        holder.bind(currentLeague)
        holder.setIsRecyclable(false)
    }

    class  DiffCallbak: DiffUtil.ItemCallback<FavouritesTable>(){
        override fun areItemsTheSame(oldItem: FavouritesTable, newItem: FavouritesTable): Boolean {
            return oldItem != newItem

        }

        override fun areContentsTheSame(oldItem: FavouritesTable, newItem: FavouritesTable): Boolean {
            return oldItem != newItem
        }

    }


}