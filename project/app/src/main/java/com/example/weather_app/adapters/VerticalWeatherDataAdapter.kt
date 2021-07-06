package com.example.weather_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_app.R
import com.example.weather_app.databinding.ItemCurrentWeatherDataBinding
import com.example.weather_app.databinding.ItemDailyForecastBinding
import com.example.weather_app.models.VerticalWeatherData

private const val DAILY_FORECAST_VIEW_TYPE = 10
private const val CURRENT_WEATHER_DATA_VIEW_TYPE = 20
class VerticalWeatherDataAdapter(
    private val data: VerticalWeatherData
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    class VerticalWeatherDataDailyForecastViewHolder(val binding: ItemDailyForecastBinding)
        : RecyclerView.ViewHolder(binding.root)

    class VerticalWeatherDataCurrentWeatherDataViewHolder(val binding: ItemCurrentWeatherDataBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        if (position<data.dailyForecastList.size){
            return DAILY_FORECAST_VIEW_TYPE
        }
        return CURRENT_WEATHER_DATA_VIEW_TYPE
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        when(viewType){
            DAILY_FORECAST_VIEW_TYPE -> {
                return VerticalWeatherDataDailyForecastViewHolder(ItemDailyForecastBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ))
            }
            else -> {
                return  VerticalWeatherDataCurrentWeatherDataViewHolder(
                    ItemCurrentWeatherDataBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            DAILY_FORECAST_VIEW_TYPE -> {
                val viewHolder = holder as VerticalWeatherDataDailyForecastViewHolder
                viewHolder.binding.apply {
                    tvDay.text = data.dailyForecastList[position].day
                    tvDailyH.text = viewHolder.itemView.context.getString(
                        R.string.secondary_temp,
                        data.dailyForecastList[position].tempH
                    )
                    tvDailyL.text = viewHolder.itemView.context.getString(
                        R.string.secondary_temp,
                        data.dailyForecastList[position].tempL
                    )
                }
            }
            CURRENT_WEATHER_DATA_VIEW_TYPE -> {
                val viewHolder = holder as VerticalWeatherDataCurrentWeatherDataViewHolder
                viewHolder.binding.apply {
                    tvDataHeaderLeft.text = data.currentWeatherDataList[position-data.dailyForecastList.size].headerLeft
                    tvDataHeaderRight.text = data.currentWeatherDataList[position-data.dailyForecastList.size].headerRight
                    tvDataValueLeft.text = data.currentWeatherDataList[position-data.dailyForecastList.size].valueLeft
                    tvDataValueRight.text = data.currentWeatherDataList[position-data.dailyForecastList.size].valueRight
                }
            }
        }
    }

    override fun getItemCount(): Int = data.getSize()
}
