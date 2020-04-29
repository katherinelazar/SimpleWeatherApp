package hu.ait.weatherreport.network

import android.telecom.Call
import hu.ait.weatherreport.data.Base
import retrofit2.http.GET
import retrofit2.http.Query

//host: https://openweathermap.org/api
// 37363c39bcdb1736b29410d6e5188bf7

interface WeatherAPI {
    @GET("/data/2.5/weather")
    fun getWeatherDetails(
        @Query("q") city: String,
        @Query("units" ) units: String,
        @Query("appid") appid: String) : retrofit2.Call<Base>
}
