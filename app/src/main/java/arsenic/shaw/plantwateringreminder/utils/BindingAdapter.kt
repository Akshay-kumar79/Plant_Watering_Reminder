package arsenic.shaw.plantwateringreminder.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import arsenic.shaw.plantwateringreminder.activities.mainActivity.PlantAdapter
import arsenic.shaw.plantwateringreminder.database.Plant

@BindingAdapter("addPlantsList")
fun addPlantsList(recyclerView: RecyclerView, data: List<Plant>?){
    val adapter = recyclerView.adapter as PlantAdapter
    if (data != null) {
        adapter.setData(data)
    }
}
