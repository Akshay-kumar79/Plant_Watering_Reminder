package arsenic.shaw.plantwateringreminder.activities.signInActivity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.TokenWatcher
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import arsenic.shaw.plantwateringreminder.R
import arsenic.shaw.plantwateringreminder.activities.mainActivity.MainActivity
import arsenic.shaw.plantwateringreminder.activities.signUpActivity.SignUpActivity
import arsenic.shaw.plantwateringreminder.databinding.ActivitySignInBinding
import com.google.android.material.snackbar.Snackbar

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var viewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[SignInViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setObservers()
        setClickListeners()

        createNotificationChannel(getString(R.string.plant_reminder_notification_channel_id), "Plant")

        setContentView(binding.root)
    }

    private fun setObservers() {
        viewModel.snackBarText.observe(this){
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }

        viewModel.isSignedIn.observe(this){
            if (it)
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun createNotificationChannel(channelID: String, channelName: String) {
        val attributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build()

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.enableLights(true)
            notificationChannel.description = "Plant watering Reminder"
            notificationChannel.lightColor = Color.RED
            notificationChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), attributes)

            val notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun setClickListeners() {
        binding.createAnAccount.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}