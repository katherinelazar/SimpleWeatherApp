package hu.ait.weatherreport.adapter

import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import hu.ait.weatherreport.MainActivity
import hu.ait.weatherreport.R
import hu.ait.weatherreport.WeatherDetailsActivity
import hu.ait.weatherreport.data.AppDatabase
import hu.ait.weatherreport.data.City
import kotlinx.android.synthetic.main.city_row.view.*
import java.util.*

class CityAdapter : RecyclerView.Adapter<CityAdapter.ViewHolder> {

    var cityItems = mutableListOf<City>()
    val context: Context

    constructor(context: Context, listCities: List<City>) {
        this.context = context
        cityItems.addAll(listCities)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.city_row, parent, false
        )

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cityItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentCity = cityItems[position]

        holder.tvCity.text = currentCity.cityName

        holder.btnDelete.setOnClickListener {
            deleteCity(holder.adapterPosition)
        }

        holder.tvCity.setOnClickListener {
            var intent = Intent(this.context, WeatherDetailsActivity::class.java)
            intent.putExtra(context.getString(R.string.key_details), currentCity.cityName)
            context.startActivity(intent)

        }
    }

    private fun deleteCity(position: Int) {
        Thread {

            AppDatabase.getInstance(context).cityDao().deleteCity(
                cityItems.get(position))

            (context as MainActivity).runOnUiThread {
                cityItems.removeAt(position)
                notifyItemRemoved(position)
            }
        }.start()
    }

    public fun addCity(city: City) {
        cityItems.add(city)

        notifyItemInserted(cityItems.lastIndex)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCity = itemView.tvCity
        val btnDelete = itemView.btnDelete
    }

}