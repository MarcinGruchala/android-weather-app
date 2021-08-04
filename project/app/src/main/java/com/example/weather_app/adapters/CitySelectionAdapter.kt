package com.example.weather_app.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_app.R
import com.example.weather_app.databinding.ItemCitySelectionBinding
import com.example.weather_app.databinding.ItemCityShortcutBinding
import com.example.weather_app.models.UnitOfMeasurement
import com.example.weather_app.models.entities.CityShortcut
import com.example.weather_app.utils.UiUtils

private const val CITY_SHORTCUT_VIEW_TYPE = 10
private const val CITY_SELECTION_VIEW_TYPE = 20
class CitySelectionAdapter(
    private var data: List<CityShortcut>,
    private val unitMode: String,
    private val itemClickListener: (CityShortcut) -> Unit,
    private val deleteButtonClickListener: (CityShortcut) -> Unit,
    private val citySearchClickListener: (String) -> Unit,
    private val unitSelectionClickListener: () -> String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class CityShortcutViewHolder(
        val binding: ItemCityShortcutBinding
        ) : RecyclerView.ViewHolder(binding.root)

    class CitySelectionViewHolder(
        val binding: ItemCitySelectionBinding
        ) : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(
        position: Int
    ): Int {
        if (position == itemCount - 1){
            return CITY_SELECTION_VIEW_TYPE
        }
        return CITY_SHORTCUT_VIEW_TYPE
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        if (viewType == CITY_SHORTCUT_VIEW_TYPE){
            return CityShortcutViewHolder(
                ItemCityShortcutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
        return CitySelectionViewHolder(
            ItemCitySelectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when(holder.itemViewType) {
            CITY_SHORTCUT_VIEW_TYPE -> bindCityShortcutViewType(holder, position)
            CITY_SELECTION_VIEW_TYPE -> bindCitySelectionViewType(holder)
        }
    }

    override fun getItemCount(): Int = data.size+1

    private fun bindCityShortcutViewType(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val viewHolder = holder as CityShortcutViewHolder
        val reversePosition = data.size - position - 1
        viewHolder.binding.root.setBackgroundResource(
            UiUtils.getCityShortcutBackground(
                data[reversePosition].icon
            )
        )
        if (reversePosition == itemCount - 2) {
            viewHolder.binding.apply {
                root.getTransition(R.id.transitionCityShortcut).setEnable(false)
                tvLocalTime.text = data[reversePosition].cityName
                tvCityName.text =  viewHolder.itemView.context.getString(
                    R.string.my_location_header
                )
                tvCityTemp.text =  viewHolder.itemView.context.getString(
                    R.string.temp,
                    data[reversePosition].temp
                )
                ivCityShortcutWeatherIcon.setImageResource(
                    UiUtils.getWeatherIcon(data[reversePosition].icon)
                )
                clCityShortcutHandle.setOnClickListener {
                    itemClickListener(data[reversePosition])
                }
            }
        } else {
            viewHolder.binding.apply {
                tvCityName.text = data[reversePosition].cityName
                tvCityTemp.text = viewHolder.itemView.context.getString(
                    R.string.temp,
                    data[reversePosition].temp
                )
                tvLocalTime.text = data[reversePosition].localTime
                ivCityShortcutWeatherIcon.setImageResource(
                    UiUtils.getWeatherIcon(data[reversePosition].icon)
                )
                clCityShortcutHandle.setOnClickListener {
                    itemClickListener(data[reversePosition])
                }
                btnDelete.setOnClickListener {
                    deleteButtonClickListener(data[reversePosition])
                }
            }
        }
    }

    private fun bindCitySelectionViewType(
        holder: RecyclerView.ViewHolder,
    ) {
        val viewHolder= holder as CitySelectionViewHolder
        viewHolder.binding.apply {
            if (unitMode == UnitOfMeasurement.METRIC.value){
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
                if(unitSelectionClickListener() == UnitOfMeasurement.METRIC.value){
                    tvFahrenheit.setTextColor(Color.GRAY)
                }
                else{
                    tvCelsius.setTextColor(Color.GRAY)
                }
            }
        }
    }
}
