package eu.lemurweb.pogodynka.model

import eu.lemurweb.pogodynka.model.api.WeatherService
import eu.lemurweb.pogodynka.model.entity.Current
import eu.lemurweb.pogodynka.model.entity.SavedCity
import eu.lemurweb.pogodynka.model.room.SavedCityDao
import eu.lemurweb.pogodynka.model.room.WeatherDatabase
import retrofit2.*

class WeatherRepository(private val savedDao: SavedCityDao) {
    val homeCity = savedDao.getHomeCity()

    suspend fun getCityByName(city: String): Current? {
        return WeatherService.api.getCityMyName(city).awaitResponse().body()
    }
    suspend fun getCityById(city: Int): Current? {
        return WeatherService.api.getCityMyId(city).awaitResponse().body()
    }
    //save city in db; before inserting new row, old rows about this city are deleted
    suspend fun saveCity(city:SavedCity){
        savedDao.deleteCity(city.api_id)
        savedDao.insertCity(city)
    }
    suspend fun insertCity(city: SavedCity) = savedDao.insertCity(city)
    //insert new city (which should have type = 1) to db. Before, old home city is restarted.
    suspend fun insertHomeCity(city: SavedCity){
        savedDao.resetHomeCity()
        savedDao.insertCity(city)
    }
    suspend fun setHomeCity(city: Int){
        savedDao.resetHomeCity()
        savedDao.updateHomeCity(city)
    }
    suspend fun deleteCity(city: Int) = savedDao.deleteCity(city)
}