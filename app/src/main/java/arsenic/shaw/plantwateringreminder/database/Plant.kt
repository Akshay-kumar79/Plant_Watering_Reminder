package arsenic.shaw.plantwateringreminder.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plant_table")
data class Plant(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    var name: String,
    var image: String,
    var wateringPeriod: Int,
    var startTime: Long   //start time and date in millisecond
)
