package com.example.weather_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CurrentWeatherDataAdapter(
    private val data: List<CurrentWeatherData>
) : RecyclerView.Adapter<CurrentWeatherDataAdapter.CurrentWeatherDataViewHolder>() {

    class CurrentWeatherDataViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView){
            val tvHeaderLeft: TextView = itemView.findViewById(R.id.tvDataHeaderLeft)
            val tvHeaderRight: TextView = itemView.findViewById(R.id.tvDataHeaderRight)
            val tvValueLeft: TextView = itemView.findViewById(R.id.tvDataValueLeft)
            val tvValueRight: TextView = itemView.findViewById(R.id.tvDataValueRight)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrentWeatherDataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_current_weather_data,
            parent,
            false
        )
        return CurrentWeatherDataViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrentWeatherDataViewHolder, position: Int) {
        holder.tvHeaderLeft.text = data[position].headerLeft
        holder.tvHeaderRight.text = data[position].headerRight
        holder.tvValueLeft.text = data[position].valueLeft
        holder.tvValueRight.text = data[position].valueRight
    }

    override fun getItemCount(): Int = data.size
}