package com.example.weather_app.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_app.R
import com.example.weather_app.models.VerticalWeatherData

private  const val DAILY_FORECAST_VIEW_TYPE = 10
private const val CURRENT_WEATHER_DATA_VIEW_TYPE = 20
class VerticalWeatherDataAdapter(
    private val data: VerticalWeatherData
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    class VerticalWeatherDataDailyForecastViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView){
            val tvDay: TextView = itemView.findViewById(R.id.tvDay)
            val tvDailyH: TextView = itemView.findViewById(R.id.tvDailyH)
            val tvDailyL: TextView = itemView.findViewById(R.id.tvDailyL)
        }

    class VerticalWeatherDataCurrentWeatherDataViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {
            val tvHeaderLeft: TextView = itemView.findViewById(R.id.tvDataHeaderLeft)
            val tvHeaderRight: TextView = itemView.findViewById(R.id.tvDataHeaderRight)
            val tvValueLeft: TextView = itemView.findViewById(R.id.tvDataValueLeft)
            val tvValueRight: TextView = itemView.findViewById(R.id.tvDataValueRight)
        }

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
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_daily_forecast,
                    parent,
                    false
                )
                return VerticalWeatherDataDailyForecastViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_current_weather_data,
                    parent,
                    false
                )
                return  VerticalWeatherDataCurrentWeatherDataViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("VerticalWeatherData", "position $position")
        Log.d("VerticalWeatherData", "viewHolder: ${holder.itemViewType}")
        when(holder.itemViewType){
            DAILY_FORECAST_VIEW_TYPE -> {
                val viewHolder = holder as VerticalWeatherDataDailyForecastViewHolder
                viewHolder.tvDay.text = data.dailyForecastList[position].day
                viewHolder.tvDailyH.text = data.dailyForecastList[position].tempH.toString()
                viewHolder.tvDailyL.text = data.dailyForecastList[position].tempL.toString()
            }
            CURRENT_WEATHER_DATA_VIEW_TYPE -> {
                val viewHolder = holder as VerticalWeatherDataCurrentWeatherDataViewHolder
                viewHolder.tvHeaderLeft.text = data.currentWeatherDataList[position-data.dailyForecastList.size].headerLeft
                viewHolder.tvHeaderRight.text = data.currentWeatherDataList[position-data.dailyForecastList.size].headerRight
                viewHolder.tvValueLeft.text = data.currentWeatherDataList[position-data.dailyForecastList.size].valueLeft
                viewHolder.tvValueRight.text = data.currentWeatherDataList[position-data.dailyForecastList.size].valueRight
            }
        }
    }

    override fun getItemCount(): Int = data.getSize()
}