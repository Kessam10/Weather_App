package com.example.weatherapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.HourlyListTempBinding
import com.example.domain.entity.HourlyWeather
import com.example.weatherapp.utils.getWeatherIcon

class HourlyTempAdapter(private var hourlyList: List<HourlyWeather>) :
    RecyclerView.Adapter<HourlyTempAdapter.HourlyTempViewHolder>() {

    inner class HourlyTempViewHolder(val binding: HourlyListTempBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyTempViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HourlyListTempBinding.inflate(inflater, parent, false)
        return HourlyTempViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyTempViewHolder, position: Int) {
        val item = hourlyList[position]
        holder.binding.time.text = item.datetime?:""
        holder.binding.temp.text = "${item.temp ?: "--"}°"
        holder.binding.weatherIconImageView.setImageResource(
            getWeatherIcon(item.icon ?: "default-icon")
        )

    }

    override fun getItemCount(): Int = hourlyList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<HourlyWeather>) {
        hourlyList = newData
        notifyDataSetChanged()
    }
}
