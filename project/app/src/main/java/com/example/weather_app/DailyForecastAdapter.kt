package com.example.weather_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DailyForecastAdapter(
    private var forecasts: List<DailyForecast>
) : RecyclerView.Adapter<DailyForecastAdapter.DailyForecastViewHolder>() {

    class DailyForecastViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView){
        val tvDay: TextView = itemView.findViewById(R.id.tvDay)
        val tvDailyH: TextView = itemView.findViewById(R.id.tvDailyH)
        val tvDailyL: TextView = itemView.findViewById(R.id.tvDailyL)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_daily_forecast,
            parent,
            false
        )
        return DailyForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {
        holder.tvDay.text = forecasts[position].day
        holder.tvDailyH.text = forecasts[position].tempH.toString()
        holder.tvDailyL.text = forecasts[position].tempL.toString()
    }

    override fun getItemCount(): Int = forecasts.size
}