package com.example.weather_app.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app.adapters.CitySelectionAdapter
import com.example.weather_app.databinding.ActivityCitySelectionBinding
import com.example.weather_app.models.CityShortcutData
import com.example.weather_app.viewmodels.CitySelectionActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

private  const val TAG = "CitySelectionActivity"
@AndroidEntryPoint
class CitySelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCitySelectionBinding
    private val viewModel: CitySelectionActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCitySelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerViewsSetup()
        updateRecyclerView()
    }

    private fun recyclerViewsSetup(){
        binding.rvCitySelection.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun updateRecyclerView(){
        val testList = listOf<CityShortcutData>(
            CityShortcutData("Bydgoszcz","12:53",30),
            CityShortcutData("Warsow","12:23",28),
            CityShortcutData("Wroclaw","12:53",32),
            CityShortcutData("Sydney","9:00",14)
        )

        binding.rvCitySelection.adapter = CitySelectionAdapter(testList) { item ->
            Log.d(TAG,"new location: ${item.cityName}")
            viewModel.updateMainWeatherForecastLocation(item.cityName)
            finish()
        }
    }
}
