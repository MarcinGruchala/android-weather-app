package com.example.weather_app.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weather_app.database.entities.CityShortcut

@Dao
interface CityShortcutDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCityShortcut(cityShortcut: CityShortcut)

    @Delete
    suspend fun deleteCityShortcut(cityShortcut: CityShortcut)

    @Query("SELECT * FROM cities_shortcuts ORDER BY id")
    fun getAllCityShortcuts(): LiveData<List<CityShortcut>>
}