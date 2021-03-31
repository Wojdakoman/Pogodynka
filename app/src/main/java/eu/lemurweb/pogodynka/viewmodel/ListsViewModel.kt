package eu.lemurweb.pogodynka.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import eu.lemurweb.pogodynka.model.room.WeatherDatabase

class ListsViewModel(application: Application): AndroidViewModel(application) {
    val savedList = WeatherDatabase.getDatabase(application).savedDao().getSaved()
    val lastSearchList = WeatherDatabase.getDatabase(application).savedDao().getLastSearch()

    val _homeCity = WeatherDatabase.getDatabase(application).savedDao().getHomeCity()
}