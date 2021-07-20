package com.example.weather_app.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView

import com.example.weather_app.databinding.ItemCitySelectionBinding
import com.example.weather_app.databinding.ItemCityShortcutBinding
import com.example.weather_app.models.CityShortcutData
import com.example.weather_app.viewmodels.CitySelectionActivityViewModel

private const val TAG = "CitySelectionAdapter"
private const val CITY_SHORTCUT_VIEW_TYPE = 10
private const val CITY_SELECTION_VIEW_TYPE = 20
class CitySelectionAdapter(
    private val data: List<CityShortcutData>,
    private val itemClickListener: (CityShortcutData) -> Unit,
    private val citySearchClickListener: (String) -> Unit,
    private val unitSelectionClickListener: () -> Unit
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
        when(holder.itemViewType){
            CITY_SHORTCUT_VIEW_TYPE ->{
                val viewHolder = holder as CityShortcutViewHolder
                viewHolder.binding.apply {
                    tvCityName.text = data[position].cityName
                    tvCityTemp.text = data[position].temp.toString()
                    tvLocalTime.text = data[position].localTime
                }
                viewHolder.itemView.setOnClickListener { itemClickListener(data[position]) }
            }
            CITY_SELECTION_VIEW_TYPE ->{
                val viewHolder= holder as CitySelectionViewHolder
                viewHolder.binding.apply {
                    tvUnitSelection.text = "°C/°F"

                    btnCitySearch.setOnClickListener {
                        val cityName = etCity.text.toString()
                        citySearchClickListener(cityName)
                        Log.d(TAG, "Search city: $cityName")
                    }

                    tvUnitSelection.setOnClickListener {
                        unitSelectionClickListener()
                        Log.d(TAG,"Unit change for")
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = data.size+1

}
