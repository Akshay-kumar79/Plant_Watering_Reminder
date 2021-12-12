package arsenic.shaw.plantwateringreminder.utils

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

class PlantAlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager = ContextCompat.getSystemService(context!!, NotificationManager::class.java) as NotificationManager
        notificationManager.sendPlantReminderNotification(context, "It's time to give water to ${intent?.getStringExtra(Constants.PLANT_NAME_FOR_NOTIFICATION)}")
    }

}