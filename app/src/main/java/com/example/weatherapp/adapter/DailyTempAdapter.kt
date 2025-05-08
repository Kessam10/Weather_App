//package com.example.weatherapp.adapter
//
//import android.annotation.SuppressLint
//import android.os.Build
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.annotation.RequiresApi
//import androidx.recyclerview.widget.RecyclerView
//import com.example.domain.entity.DailyWeatherEntity
//import com.example.weatherapp.databinding.DailyWeatherTempBinding
//import com.example.weatherapp.utils.getWeatherIcon
//import java.time.LocalDate
//import java.time.format.DateTimeFormatter
//import java.util.Locale
//
//class DailyTempAdapter(private var dailyList: List<DailyWeatherEntity>) :
//    RecyclerView.Adapter<DailyTempAdapter.DailyTempViewHolder>() {
//    inner class DailyTempViewHolder(val binding: DailyWeatherTempBinding):
//            RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyTempViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        val binding = DailyWeatherTempBinding.inflate(inflater,parent,false)
//        return DailyTempViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: DailyTempViewHolder, position: Int) {
//        val item = dailyList[position]
//        holder.binding.date.text = getDayOfWeek( item.datetime?:"")
//        holder.binding.weatherIconImageView.setImageResource(
//            getWeatherIcon(item.icon?:"default-icon")
//        )
//        holder.binding.temp.text = "${item.temp ?: "--"}°"
//    }
//
//    override fun getItemCount(): Int = dailyList.size
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun updateDate(newData:List<DailyWeatherEntity>){
//        dailyList = newData
//        notifyDataSetChanged()
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun getDayOfWeek(dateString: String): String {
//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
//        val date = LocalDate.parse(dateString, formatter)
//        return date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
//    }
//
//
//}
package com.example.weatherapp.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.DailyWeatherEntity
import com.example.weatherapp.databinding.DailyWeatherTempBinding
import com.example.weatherapp.utils.getWeatherIcon
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class DailyTempAdapter(private var dailyList: List<DailyWeatherEntity>) :
    RecyclerView.Adapter<DailyTempAdapter.DailyTempViewHolder>() {

    inner class DailyTempViewHolder(val binding: DailyWeatherTempBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyTempViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DailyWeatherTempBinding.inflate(inflater, parent, false)
        return DailyTempViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DailyTempViewHolder, position: Int) {
        val item = dailyList[position]

        val dayLabel = item.datetime?.let { dateString ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getDayOfWeek(dateString)
            } else {
                dateString
            }
        } ?: item.datetime?:""

        holder.binding.date.text = dayLabel
        holder.binding.weatherIconImageView.setImageResource(
            getWeatherIcon(item.icon ?: "default-icon")
        )
        holder.binding.temp.text = "${item.temp ?: "--"}°"
    }

    override fun getItemCount(): Int = dailyList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateDate(newData: List<DailyWeatherEntity>) {
        dailyList = newData
        notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDayOfWeek(dateString: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
        val date = LocalDate.parse(dateString, formatter)
        val dayName = date.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault())
        val dayOfMonth = date.dayOfMonth.toString().padStart(2, '0')
        return "$dayName $dayOfMonth"
    }
}
