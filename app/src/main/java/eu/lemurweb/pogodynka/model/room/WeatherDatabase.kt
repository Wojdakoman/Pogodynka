package eu.lemurweb.pogodynka.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import eu.lemurweb.pogodynka.model.entity.SavedCity

@Database(entities = [SavedCity::class], version = 1, exportSchema = false)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun savedDao(): SavedCityDao

    companion object {
        @Volatile
        private var instance: WeatherDatabase? = null

        fun getDatabase(context: Context): WeatherDatabase {
            var tempInstance = instance

            if(tempInstance == null){
                synchronized(this){
                    var newInstance = Room.databaseBuilder(
                        context.applicationContext,
                        WeatherDatabase::class.java,
                        "weatherDB"
                    ).fallbackToDestructiveMigration().build()
                    instance = newInstance
                    return newInstance
                }
            } else return tempInstance
        }
    }
}