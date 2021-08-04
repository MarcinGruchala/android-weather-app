package com.example.weather_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_app.R
import com.example.weather_app.databinding.ItemHourlyForecastBinding
import com.example.weather_app.models.HourlyForecastData

class HourlyForecastAdapter(
    private var forecasts: List<HourlyForecastData>
) : RecyclerView.Adapter<HourlyForecastAdapter.HourlyForecastViewHolder>() {

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
                R.string.secondary_temp,
                forecasts[position].temp
            )
            ivHourlyIcon.setImageResource(forecasts[position].icon)
        }
    }

    override fun getItemCount(): Int = forecasts.size
}
