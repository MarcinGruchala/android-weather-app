package com.example.weather_app.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_app.R
import com.example.weather_app.databinding.ItemCitySelectionBinding
import com.example.weather_app.databinding.ItemCityShortcutBinding
import com.example.weather_app.models.CityShortcutData
import com.example.weather_app.models.UnitOfMeasurement

private const val TAG = "CitySelectionAdapter"
private const val CITY_SHORTCUT_VIEW_TYPE = 10
private const val CITY_SELECTION_VIEW_TYPE = 20
private const val CURRENT_LOCATION_VIEW_TYPE = 30
class CitySelectionAdapter(
    private val data: List<CityShortcutData>,
    private val unitMode: UnitOfMeasurement,
    private val itemClickListener: (CityShortcutData) -> Unit,
    private val citySearchClickListener: (String) -> Unit,
    private val unitSelectionClickListener: () -> UnitOfMeasurement
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    class CityShortcutViewHolder(val binding: ItemCityShortcutBinding)
        : RecyclerView.ViewHolder(binding.root)

    class CitySelectionViewHolder(val binding: ItemCitySelectionBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        if (position == itemCount-1){
            return CITY_SELECTION_VIEW_TYPE
        }
        return CITY_SHORTCUT_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == CITY_SHORTCUT_VIEW_TYPE){
            return CityShortcutViewHolder(ItemCityShortcutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
        }
        return CitySelectionViewHolder(ItemCitySelectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //Log.d(TAG, "Position: $position, view type: ${holder.itemViewType}")
        when(holder.itemViewType){
            CITY_SHORTCUT_VIEW_TYPE ->{
                val viewHolder = holder as CityShortcutViewHolder
                val reversePosition = data.size - position - 1
                if (reversePosition == itemCount-2){
                    //Log.d(TAG, "MY LOCATION")
                    viewHolder.binding.apply {
                        tvLocalTime.text = data[reversePosition].cityName
                        tvCityName.text = "My Location"
                        tvCityTemp.text =  viewHolder.itemView.context.getString(
                            R.string.main_temp,
                            data[reversePosition].temp
                        )
                        ivCityShortcutWeatherIcon.setImageResource(
                            data[reversePosition].icon
                        )
                    }
                }
                else{
                    viewHolder.binding.apply {
                        tvCityName.text = data[reversePosition].cityName
                        tvCityTemp.text = viewHolder.itemView.context.getString(
                            R.string.main_temp,
                            data[reversePosition].temp
                        )
                        tvLocalTime.text = data[reversePosition].localTime
                        ivCityShortcutWeatherIcon.setImageResource(
                            data[reversePosition].icon
                        )
                    }

                }
                viewHolder.itemView.setOnClickListener { itemClickListener(data[reversePosition]) }
            }

            CITY_SELECTION_VIEW_TYPE ->{
                val viewHolder= holder as CitySelectionViewHolder
                viewHolder.binding.apply {

                    if (unitMode == UnitOfMeasurement.METRIC){
                        tvFahrenheit.setTextColor(Color.GRAY)
                    }
                    else{
                        tvCelsius.setTextColor(Color.GRAY)
                    }

                    btnCitySearch.setOnClickListener {
                        val cityName = etCity.text.toString()
                        citySearchClickListener(cityName)
                    }

                    clUnitSelection.setOnClickListener {
                        if(unitSelectionClickListener() == UnitOfMeasurement.METRIC){
                            tvFahrenheit.setTextColor(Color.GRAY)
                        }
                        else{
                            tvCelsius.setTextColor(Color.GRAY)
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = data.size+1


}
