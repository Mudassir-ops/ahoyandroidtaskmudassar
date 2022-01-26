package com.example.ahoyandroidtaskmudassar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ahoyandroidtaskmudassar.databinding.ForcastLayoutBinding
import com.example.ahoyandroidtaskmudassar.model.DailyWeatherTable


class WeeklyWeatherForcastAdapter(private val callback: (Int) -> Unit) : ListAdapter<DailyWeatherTable, WeeklyWeatherForcastAdapter.ViewHolder>(DiffCallbak()){

    class ViewHolder(private val binding: ForcastLayoutBinding, val callback: (Int) -> Unit): RecyclerView.ViewHolder(binding.root) {

        fun bind(daily: DailyWeatherTable){
            binding.apply {

                tvTime.text=daily.day
                tvTmep.text = daily.temp
                tvWind.text = "${daily.wind_speed} K/m"

            }
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

    class  DiffCallbak: DiffUtil.ItemCallback<DailyWeatherTable>(){
        override fun areItemsTheSame(oldItem: DailyWeatherTable, newItem: DailyWeatherTable): Boolean {
            return oldItem != newItem

        }

        override fun areContentsTheSame(oldItem: DailyWeatherTable, newItem: DailyWeatherTable): Boolean {
            return oldItem != newItem
        }

    }


}