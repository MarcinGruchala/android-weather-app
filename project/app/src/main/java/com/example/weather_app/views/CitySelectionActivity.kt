package com.example.weather_app.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app.adapters.CitySelectionAdapter
import com.example.weather_app.databinding.ActivityCitySelectionBinding
import com.example.weather_app.models.CityShortcutData
import com.example.weather_app.viewmodels.CitySelectionActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "CitySelectionActivity"
@AndroidEntryPoint
class CitySelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCitySelectionBinding
    private val viewModel: CitySelectionActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCitySelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getCitySelectionList()
        recyclerViewsSetup()

        val citySelectionListObserver = Observer<MutableList<CityShortcutData>> { _ ->
            updateRecyclerView()
        }
        viewModel.citySelectionList.observe(this,citySelectionListObserver)


        val isCitiesListUpdatedObserver = Observer<Boolean> { newValue ->
            if (newValue){
                updateRecyclerView()
                viewModel.isCitiesListUpdated.value = false
            }
        }
        viewModel.isCitiesListUpdated.observe(this,isCitiesListUpdatedObserver)
    }

    private fun recyclerViewsSetup(){
        binding.rvCitySelection.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun updateRecyclerView(){

        binding.rvCitySelection.adapter = CitySelectionAdapter(viewModel.citySelectionList.value!!,
        itemClickListener = { item ->
            Log.d(TAG,"new location: ${item.cityName}")
            viewModel.updateMainWeatherForecastLocation(item.cityName)
            finish()

        },
        citySearchClickListener = { cityName ->
            viewModel.addNewLocationToCitySelectionList(cityName)
        })
    }
}
