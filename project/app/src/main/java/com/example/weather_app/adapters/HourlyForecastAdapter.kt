package com.example.weather_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_app.data.HourlyForecastData
import com.example.weather_app.R

class HourlyForecastAdapter(
    var forecasts: List<HourlyForecastData>
) : RecyclerView.Adapter<HourlyForecastAdapter.HourlyForecastViewHolder>() {

    class HourlyForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHour: TextView = itemView.findViewById(R.id.tvHour)
        val tvHourlyTemp: TextView = itemView.findViewById(R.id.tvHourlyTemp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyForecastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_hourly_forecast,
            parent,
            false
        )
        return HourlyForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: HourlyForecastViewHolder, position: Int) {
        holder.tvHour.text = forecasts[position].hour
        holder.tvHourlyTemp.text = forecasts[position].temp.toString()
    }

    override fun getItemCount(): Int = forecasts.size
}