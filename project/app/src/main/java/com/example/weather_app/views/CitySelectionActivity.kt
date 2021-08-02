package com.example.weather_app.views

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app.R
import com.example.weather_app.adapters.CitySelectionAdapter
import com.example.weather_app.databinding.ActivityCitySelectionBinding
import com.example.weather_app.models.entities.CityShortcut
import com.example.weather_app.utils.UiUtils
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

        recyclerViewsSetup()

        val citySelectionListObserver = Observer<MutableList<CityShortcut>> {
            updateStatusBarColor()
            updateRecyclerView()
        }
        viewModel.citySelectionList.observe(this,citySelectionListObserver)

    }

    private fun updateStatusBarColor(){
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        window.statusBarColor =  ContextCompat.getColor(
            this,
            R.color.transparent
        )

        window.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this,
                UiUtils.getCityShortcutBackground(viewModel.citySelectionList.value!!.last().icon)
            )
        )
    }

    private fun recyclerViewsSetup(){
        binding.rvCitySelection.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )

        binding.rvCitySelection.addItemDecoration(
            DividerItemDecoration(this,DividerItemDecoration.VERTICAL)
        )

    }

    private fun updateRecyclerView(){
        binding.rvCitySelection.adapter = CitySelectionAdapter(viewModel.citySelectionList.value!!,
            viewModel.getUnitMode(),
            itemClickListener = { item ->
                Log.d(TAG,"new location: ${item.cityName}")
                viewModel.updateMainWeatherForecastLocation(item.cityName)
                finish()
                                },
            deleteButtonClickListener = { cityShortcut ->
                viewModel.deleteCityShortCut(cityShortcut)
            },
            citySearchClickListener = { cityName ->
                viewModel.addNewCityShortCut(cityName)
                                      },
            unitSelectionClickListener = {
                viewModel.changeUnit()
            }
        )
    }

}
