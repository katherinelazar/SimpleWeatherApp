package hu.ait.weatherreport

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import hu.ait.weatherreport.data.Base
import hu.ait.weatherreport.network.WeatherAPI
import kotlinx.android.synthetic.main.activity_weather_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat

class WeatherDetailsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_details)

        var cityName = ""
        val extras = intent.extras
        if (extras != null) {
            cityName = extras[getString(R.string.key_details_intent)].toString()
        }

        etCityName.text = cityName

        var retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var weatherAPI = retrofit.create(WeatherAPI::class.java)

        val call = weatherAPI.getWeatherDetails(
            cityName,
            getString(R.string.units_type),
            getString(R.string.weather_api_key)
        )

        call.enqueue(object : Callback<Base> {
            override fun onResponse(call: Call<Base>, response: Response<Base>) {
                insertValues(response)
            }
            override fun onFailure(call: Call<Base>, t: Throwable) {
                currentTempValue.text = t.message
            }
        })
    }

    fun unixToDate(timeStamp: Number?) : String? {
        val time = java.util.Date(timeStamp!!.toLong() * 1000)
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(time)
    }

    fun insertValues(response: Response<Base>) {

        var weatherResult = response.body()

        if (response.code() != 404) {
            weatherCategory.text = "${weatherResult?.weather?.get(0)?.main}"
            currentTempValue.text = "Current Temperature: ${weatherResult?.main?.temp}"
            minMax.text =
                "Min: ${weatherResult?.main?.temp_min} Max: ${weatherResult?.main?.temp_max}"
            pressure.text = "Pressure: ${weatherResult?.main?.pressure}"
            humidity.text = "Humidity: ${weatherResult?.main?.humidity}"

            var riseTime = unixToDate(weatherResult?.sys?.sunrise)
            var setTime = unixToDate(weatherResult?.sys?.sunset)

            sunriseSunset.text = "Sunrise: " + riseTime + " Sunset: " + setTime
            descriptionWeather.text =
                "Description: ${weatherResult?.weather?.get(0)?.description}"

            Glide.with(this@WeatherDetailsActivity)
                .load(
                    ("https://openweathermap.org/img/w/" +
                            weatherResult?.weather?.get(0)?.icon
                            + ".png")
                )
                .into(iconWeather)
        }
    }
}
