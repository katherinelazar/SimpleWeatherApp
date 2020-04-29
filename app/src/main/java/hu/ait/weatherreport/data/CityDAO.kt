package hu.ait.weatherreport.data

import androidx.room.*

@Dao
interface CityDAO {
    @Query("SELECT * FROM city")
    fun getAllCities(): List<City>

    @Insert
    fun insertCity(city: City) : Long

    @Delete
    fun deleteCity(city: City)

    @Update
    fun updateCity(city: City)
}