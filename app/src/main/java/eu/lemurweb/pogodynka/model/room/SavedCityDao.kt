package eu.lemurweb.pogodynka.model.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import eu.lemurweb.pogodynka.model.entity.SavedCity

@Dao
interface SavedCityDao {
    @Insert
    suspend fun insertCity(city: SavedCity)

    @Query("DELETE FROM cities WHERE api_id = :city AND type = 0")
    suspend fun deleteCity(city: Int)

    @Query("SELECT * FROM cities WHERE type = 1 LIMIT 1")
    fun getHomeCity(): LiveData<SavedCity>

    @Query("UPDATE cities SET type = 0 WHERE type = 1")
    suspend fun resetHomeCity()

    @Query("UPDATE cities SET type = 1 WHERE id = :city")
    suspend fun updateHomeCity(city: Int)

    @Query("SELECT * FROM cities WHERE type = 2")
    fun getSaved(): LiveData<List<SavedCity>>

    @Query("SELECT * FROM cities WHERE type = 0 ORDER BY id DESC LIMIT 5")
    fun getLastSearch(): LiveData<List<SavedCity>>

    /*
    @Query("SELECT * FROM students WHERE id = :id")
    suspend fun getStudent(id: Int): Student
     */
}