package com.example.weather_app.persistence.shortcut

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weather_app.persistence.shortcut.CityShortcutEntity

@Dao
interface CityShortcutDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCityShortcut(cityShortcutEntity: CityShortcutEntity)

    @Delete
    suspend fun deleteCityShortcut(cityShortcutEntity: CityShortcutEntity)

    @Query("SELECT * FROM cities_shortcuts ORDER BY id")
    fun getAllCityShortcuts(): LiveData<List<CityShortcutEntity>>
}
