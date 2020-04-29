package hu.ait.weatherreport.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "city")
data class City(
    @PrimaryKey(autoGenerate = true) var cityId : Long?,
    @ColumnInfo(name = "cityName") var cityName: String
) : Serializable