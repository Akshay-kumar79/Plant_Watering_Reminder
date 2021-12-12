package arsenic.shaw.plantwateringreminder.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import arsenic.shaw.plantwateringreminder.R
import arsenic.shaw.plantwateringreminder.activities.mainActivity.MainActivity


private const val PLANT_NOTIFICATION_ID = 0

fun NotificationManager.sendPlantReminderNotification(context: Context, contentText: String) {

    val notifyIntent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val notifyPendingIntent = PendingIntent.getActivity(
        context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
    )

    val notificationBuilder = NotificationCompat.Builder(context, context.getString(R.string.plant_reminder_notification_channel_id))
        .setSmallIcon(R.drawable.ic_round_access_alarm_24)
        .setContentTitle("Plant Watering Reminder")
        .setContentText(contentText)
        .setContentIntent(notifyPendingIntent)
        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    notify(PLANT_NOTIFICATION_ID, notificationBuilder.build())
}