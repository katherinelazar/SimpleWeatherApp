package hu.ait.weatherreport

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import hu.ait.weatherreport.data.Base
import hu.ait.weatherreport.network.WeatherAPI
import kotlinx.android.synthetic.main.activity_weather_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        var retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var weatherAPI = retrofit.create(WeatherAPI::class.java)
        val extras = intent.extras
        var cityList = extras?.getStringArray(getString(R.string.key_maps))

        addMarkers(cityList, weatherAPI)

    }

    fun addMarkers(cityList: Array<String>?, weatherAPI: WeatherAPI) {
        if (cityList != null ) {
            for (city in cityList) {

                if (city != null) {
                    val call = weatherAPI.getWeatherDetails(
                        city,
                        getString(R.string.units_type),
                        getString(R.string.weather_api_key)
                    )

                    call.enqueue(object : Callback<Base> {
                        override fun onResponse(call: Call<Base>, response: Response<Base>) {
                            var weatherResult = response.body()
                            if (response.code() != 404) {
                                var lat = weatherResult?.coord?.lat
                                var lon = weatherResult?.coord?.lon
                                val coords = LatLng(lat!!.toDouble(), lon!!.toDouble())
                                mMap.addMarker(MarkerOptions().position(coords).title(city))
                            }
                        }
                        override fun onFailure(call: Call<Base>, t: Throwable) {
                            currentTempValue.text = t.message
                        }
                    })
                }
            }
        }
    }
}
