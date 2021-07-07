package com.example.weather_app.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app.adapters.CitySelectionAdapter
import com.example.weather_app.databinding.ActivityCitySelectionBinding
import com.example.weather_app.models.CityShortcutData

class CitySelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCitySelectionBinding
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
        val testList = listOf(
            CityShortcutData("Bydgoszcz","12:53",30)
        )
        binding.rvCitySelection.adapter = CitySelectionAdapter(testList)
    }
}
