package com.example.weather_app

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

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
        if (position<10){
            return 0
        }
        return 1
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        when(viewType){
            0 -> {
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
            0 -> {
                val viewHolder = holder as VerticalWeatherDataDailyForecastViewHolder
                viewHolder.tvDay.text = data.dailyForecastList[position].day
                viewHolder.tvDailyH.text = data.dailyForecastList[position].tempH.toString()
                viewHolder.tvDailyL.text = data.dailyForecastList[position].tempL.toString()
            }
            1 -> {
                val viewHolder = holder as VerticalWeatherDataCurrentWeatherDataViewHolder
                viewHolder.tvHeaderLeft.text = data.currentWeatherDataList[position-10].headerLeft
                viewHolder.tvHeaderRight.text = data.currentWeatherDataList[position-10].headerRight
                viewHolder.tvValueLeft.text = data.currentWeatherDataList[position-10].valueLeft
                viewHolder.tvValueRight.text = data.currentWeatherDataList[position-10].valueRight
            }
        }
    }

    override fun getItemCount(): Int = data.getSize()
}