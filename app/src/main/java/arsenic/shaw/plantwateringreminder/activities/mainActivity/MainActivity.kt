package arsenic.shaw.plantwateringreminder.activities.mainActivity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import arsenic.shaw.plantwateringreminder.R
import arsenic.shaw.plantwateringreminder.activities.addPlantActivity.AddPlantActivity
import arsenic.shaw.plantwateringreminder.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "My Plants"

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setClickListeners()
        setPlantRecyclerView()

        setContentView(binding.root)
    }

    private fun setPlantRecyclerView() {
        val gridLayoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        binding.plantRecyclerView.layoutManager = gridLayoutManager

        val plantAdapter = PlantAdapter(PlantLongClickListener { id ->
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Delete")
            dialog.setMessage("are you sure you want to delete this plant?")
            dialog.setPositiveButton("ok") { _, _ ->
                viewModel.deletePlant(id)
            }
            dialog.setNegativeButton("cancel", null)
            dialog.setIcon(R.drawable.ic_round_delete_24)
            dialog.show()
            true
        })
        binding.plantRecyclerView.adapter = plantAdapter
    }

    private fun setClickListeners() {
        binding.addPlantButton.setOnClickListener {
            startActivity(Intent(this, AddPlantActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val deleteMenuItem = menu?.add("Delete All")
        deleteMenuItem?.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.title == "Delete All"){
            viewModel.deleteAllPlant()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }
}