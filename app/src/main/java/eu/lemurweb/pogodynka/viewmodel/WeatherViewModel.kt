package eu.lemurweb.pogodynka.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.lemurweb.pogodynka.model.WeatherRepository
import eu.lemurweb.pogodynka.model.entity.Current
import eu.lemurweb.pogodynka.model.entity.SavedCity
import eu.lemurweb.pogodynka.model.room.WeatherDatabase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class WeatherViewModel(application: Application): AndroidViewModel(application) {
    private var weatherRepository = WeatherRepository(WeatherDatabase.getDatabase(application).savedDao())

    var homeCity = weatherRepository.homeCity
    val isSetHomeCity: Boolean
        get() = homeCity.value != null

    private var _weather: MutableLiveData<Current> = MutableLiveData()
    val weather: LiveData<Current>
        get() = _weather

    val shouldLoadWeather: Boolean
        get() {
            return weather.value == null
        }

    val showProgress = MutableLiveData<Boolean>()

    val dayName: String
        get() {
            val calendar = Calendar.getInstance()
            val format = SimpleDateFormat("EEEE")
            return format.format(Date(calendar.timeInMillis))
        }

    val currentDate: String
        get() {
            val calendar = Calendar.getInstance()
            val format = SimpleDateFormat("dd.MM.yyyy")
            return format.format(Date(calendar.timeInMillis))
        }

    fun getCurrentWeather(cityName: String = "null", cityId: Int = -1){
        viewModelScope.launch {
            showProgress.value = true
            if(cityId == -1) _weather.value = weatherRepository.getCityByName(cityName)
            else _weather.value = weatherRepository.getCityById(cityId)
            showProgress.value = false
        }
    }

    fun getCurrentWeatherInHome(){
        getCurrentWeather(cityId = homeCity.value?.api_id!!)
    }

    //change wind direction given in degree to appropriate label
    fun getWindDirection(degree: Int): String {
        val calc: Int = ((degree / 22.5) + 0.5).roundToInt()
        val directions = listOf("N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW")
        return directions[(calc % 16)]
    }
    //change time in unix to string HH:mm
    fun unixToString(unix: Long): String {
        val format = SimpleDateFormat("HH:mm")
        return format.format(Date(unix*1000))
    }
    //save city in last serach list
    fun saveCity(){
        viewModelScope.launch {
            if(homeCity.value?.api_id != weather.value?.id){
                weatherRepository.deleteCity(weather.value?.id!!)
                weatherRepository.insertCity(SavedCity(weather.value?.name!!, weather.value?.id!!, 0))
            }
        }
    }

    fun setCurrentAsHome(){
        viewModelScope.launch {
            weatherRepository.insertHomeCity(SavedCity(weather.value?.name!!, weather.value?.id!!, 1))
        }
    }
    //save city in favourite list
    fun saveCurrent(){
        viewModelScope.launch {
            weatherRepository.saveCity(SavedCity(weather.value?.name!!, weather.value?.id!!, 2))
        }
    }
}