package eu.lemurweb.pogodynka.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class SavedCity(var name: String, var api_id: Int, var type: Int){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
/*type:
0 - nothing
1 - home
2 - fav
 */