package arsenic.shaw.plantwateringreminder.activities.mainActivity

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import arsenic.shaw.plantwateringreminder.database.PlantDatabase
import arsenic.shaw.plantwateringreminder.utils.Constants
import arsenic.shaw.plantwateringreminder.utils.PlantAlarmReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = PlantDatabase.getInstance(application).plantDao

    private val alarmManager = application.getSystemService(AlarmManager::class.java) as AlarmManager

    val allPlants = database.getAllPlants()

    fun deleteAllPlant() = viewModelScope.launch {

        val allPlantList = allPlants.value

        if (allPlantList != null) {
            for (plant in allPlantList) {
                val notifyIntent = Intent(getApplication(), PlantAlarmReceiver::class.java)
                notifyIntent.putExtra(Constants.PLANT_NAME_FOR_NOTIFICATION, plant.name)
                val pendingIntent = PendingIntent.getBroadcast(getApplication(), plant.id.toInt(), notifyIntent, 0)
                alarmManager.cancel(pendingIntent)
            }
        }

        withContext(Dispatchers.IO) {
            database.deleteAll()
        }
    }

    fun deletePlant(id: Long) = viewModelScope.launch {

        val notifyIntent = Intent(getApplication(), PlantAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(getApplication(), id.toInt(), notifyIntent, 0)
        alarmManager.cancel(pendingIntent)

        withContext(Dispatchers.IO) {
            database.delete(id)
        }
    }

}