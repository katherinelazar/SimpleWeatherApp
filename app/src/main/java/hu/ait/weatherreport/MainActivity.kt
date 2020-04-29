package hu.ait.weatherreport

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import hu.ait.weatherreport.adapter.CityAdapter
import hu.ait.weatherreport.data.AppDatabase
import hu.ait.weatherreport.data.City
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AddCityDialog.CityHandler {

    lateinit var cityAdapter: CityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            showAddCityDialog()
        }

        initRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_map -> {
            var intent = Intent(this, MapsActivity::class.java)

            var arrayCities = arrayOfNulls<String>(cityAdapter.cityItems.size)
            for (i in 0 until cityAdapter.cityItems.size) {
                arrayCities[i] = cityAdapter.cityItems[i].cityName
            }
            intent.putExtra(getString(R.string.key_maps), arrayCities)
            startActivity(intent)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun initRecyclerView() {
        Thread {
            var cityList = AppDatabase.getInstance(this).cityDao().getAllCities()

            runOnUiThread {
                cityAdapter = CityAdapter(this, cityList)
                recyclerCities.adapter = cityAdapter

            }
        }.start()
    }

    fun showAddCityDialog() {
        AddCityDialog().show(supportFragmentManager, "Dialog")
    }

    fun saveCity(city: City) {
        Thread {
            city.cityId = AppDatabase.getInstance(this).cityDao().insertCity(city)

            runOnUiThread {
                cityAdapter.addCity(city)
            }
        }.start()
    }

    override fun cityCreated(city: City) {
        saveCity(city)
    }


}
