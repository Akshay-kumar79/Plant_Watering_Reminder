package arsenic.shaw.plantwateringreminder.activities.addPlantActivity

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import arsenic.shaw.plantwateringreminder.database.Plant
import arsenic.shaw.plantwateringreminder.database.PlantDatabase
import arsenic.shaw.plantwateringreminder.utils.Constants
import arsenic.shaw.plantwateringreminder.utils.PlantAlarmReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AddPlantViewModel(application: Application) : AndroidViewModel(application) {

    private val database = PlantDatabase.getInstance(application).plantDao

    private val alarmManager = application.getSystemService(AlarmManager::class.java) as AlarmManager


    val plantName = MutableLiveData<String>()
    val wateringPeriod = MutableLiveData<String>()
    val hour = MutableLiveData<Int>()
    val minute = MutableLiveData<Int>()
    var encodedImage: String? = null

    private val _isPlantAdded = MutableLiveData<Boolean>()
    val isPlantAdded: LiveData<Boolean>
        get() = _isPlantAdded

    private val _snackBarText = MutableLiveData<String>()
    val snackBarText: LiveData<String>
        get() = _snackBarText

    fun onTickClicked() = viewModelScope.launch {
        if (isValidPlantDetails()) {
            val c = Calendar.getInstance()
            c.set(Calendar.HOUR_OF_DAY, hour.value!!)
            c.set(Calendar.MINUTE, minute.value!!)
            c.set(Calendar.SECOND, 0)
            c.set(Calendar.MILLISECOND, 0)

            if (!c.after(Calendar.getInstance())) {
                c.add(Calendar.DATE, 1)
            }

            var id: Long
            withContext(Dispatchers.IO) {
                id = database.insert(
                    Plant(
                        name = plantName.value!!,
                        startTime = c.timeInMillis,
                        image = encodedImage!!,
                        wateringPeriod = wateringPeriod.value!!.toInt()
                    )
                )
            }

            val notifyIntent = Intent(getApplication(), PlantAlarmReceiver::class.java)
            notifyIntent.putExtra(Constants.PLANT_NAME_FOR_NOTIFICATION, plantName.value!!)
            val pendingIntent = PendingIntent.getBroadcast(getApplication(), id.toInt(), notifyIntent, 0)

            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                c.timeInMillis,
                AlarmManager.INTERVAL_DAY * wateringPeriod.value!!.toLong(),
                pendingIntent
            )

            _isPlantAdded.value = true
        }
    }


    private fun isValidPlantDetails(): Boolean {

        if (encodedImage == null) {
            _snackBarText.value = "Select image"
            return false
        } else if (plantName.value == null || plantName.value!!.trim().isEmpty()) {
            _snackBarText.value = "Enter plant name"
            return false
        } else if (wateringPeriod.value == null || wateringPeriod.value!!.trim().isEmpty()) {
            _snackBarText.value = "Enter watering period"
            return false
        } else if (wateringPeriod.value.toString().toInt() < 1) {
            _snackBarText.value = "Watering period should be greater"
            return false
        }
        return true
    }


}