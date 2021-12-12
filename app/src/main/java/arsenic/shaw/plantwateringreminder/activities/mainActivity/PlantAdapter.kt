package arsenic.shaw.plantwateringreminder.activities.mainActivity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import arsenic.shaw.plantwateringreminder.database.Plant
import arsenic.shaw.plantwateringreminder.databinding.ItemForPlantListBinding
import arsenic.shaw.plantwateringreminder.utils.Constants
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

const val MILLISECOND_IN_A_DAY = 86400000L

class PlantAdapter(private val plantLongClickListener: PlantLongClickListener) : RecyclerView.Adapter<PlantAdapter.ViewHolder>() {

    private var plants: List<Plant> = ArrayList()

    fun setData(plants: List<Plant>) {
        this.plants = plants
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(plants[position], plantLongClickListener)
    }

    override fun getItemCount(): Int {
        return plants.size
    }

    class ViewHolder(private val binding: ItemForPlantListBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemForPlantListBinding.inflate(inflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(plant: Plant, plantLongClickListener: PlantLongClickListener) {
            binding.plant = plant
            binding.plantClickListener = plantLongClickListener
            binding.plantImage.setImageBitmap(Constants.decodeImage(plant.image))
            binding.plantName.text = plant.name
            binding.wateringPeriod.text = getWateringPeriodText(plant.wateringPeriod)
            binding.nextReminder.text = getNextReminderText(plant.startTime, plant.wateringPeriod)
            binding.executePendingBindings()
        }

        private fun getNextReminderText(startTime: Long, wateringPeriod: Int): String {

            val startT = Calendar.getInstance()
            startT.timeInMillis = startTime

            var next = Calendar.getInstance()

            val diff = Calendar.getInstance().timeInMillis - startT.timeInMillis
            if (diff > 0) {
                val x = (diff / (wateringPeriod.toLong() * MILLISECOND_IN_A_DAY)) + 1
                next.timeInMillis = startT.timeInMillis + (wateringPeriod * MILLISECOND_IN_A_DAY * x)
            } else {
                next.timeInMillis = startT.timeInMillis
            }

            if (next.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR) && next.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
                return "Today, ${getFormattedTime(next)}"
            } else if (next.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1 && next.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
                return "Yesterday, ${getFormattedTime(next)}"
            } else {
                return "${getFormattedDate(next)}, ${getFormattedTime(next)}"
            }

        }

        private fun getWateringPeriodText(wateringPeriod: Int): String {
            return if (wateringPeriod < 2) {
                "Everyday"
            } else {
                "$wateringPeriod days"
            }
        }

        private fun getFormattedTime(calendar: Calendar): String {
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            var hourText = hour.toString()
            var minuteText = minute.toString()

            if (hour < 10)
                hourText = "0$hour"

            if (minute < 10) {
                minuteText = "0$minute"
            }

            return "$hourText:$minuteText"
        }

        private fun getFormattedDate(cal: Calendar): String {
            val formatter = SimpleDateFormat("dd MMM, yyy", Locale.getDefault())
            return formatter.format(cal.time)
        }

    }

}

class PlantLongClickListener(val clickListener: (id: Long) -> Boolean) {
    fun onClick(plant: Plant) = clickListener(plant.id)
}