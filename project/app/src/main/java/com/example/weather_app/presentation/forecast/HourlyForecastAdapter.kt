package com.example.weather_app.presentation.forecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_app.R
import com.example.weather_app.databinding.ItemHourlyForecastBinding
import com.example.weather_app.presentation.forecast.HourlyForecastAdapter.HourlyForecastViewHolder
import com.example.weather_app.domain.forecast.HourlyForecast

class HourlyForecastAdapter(
    private var forecasts: List<HourlyForecast>
) : RecyclerView.Adapter<HourlyForecastViewHolder>() {

    class HourlyForecastViewHolder(
        val binding: ItemHourlyForecastBinding
        ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HourlyForecastViewHolder {
        return HourlyForecastViewHolder(
            ItemHourlyForecastBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: HourlyForecastViewHolder,
        position: Int
    ) {
        holder.binding.apply {
            tvHour.text = forecasts[position].hour
            tvHourlyTemp.text = holder.itemView.context.getString(
                R.string.temp,
                forecasts[position].temp
            )
            ivHourlyIcon.setImageResource(forecasts[position].icon)
        }
    }

    override fun getItemCount(): Int = forecasts.size
}
