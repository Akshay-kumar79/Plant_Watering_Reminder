package arsenic.shaw.plantwateringreminder.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PlantDao {

    @Insert
    fun insert(plant: Plant): Long

    @Query("select * from plant_table where id == :id")
    fun getPlant(id: Long): LiveData<Plant>

    @Query("delete from plant_table where id == :id")
    fun delete(id: Long)

    @Query("select * from plant_table order by id desc")
    fun getAllPlants(): LiveData<List<Plant>>

    @Query("delete from plant_table")
    fun deleteAll()
}