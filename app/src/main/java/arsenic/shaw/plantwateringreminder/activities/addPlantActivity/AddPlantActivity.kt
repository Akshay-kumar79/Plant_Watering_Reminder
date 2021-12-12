package arsenic.shaw.plantwateringreminder.activities.addPlantActivity

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import arsenic.shaw.plantwateringreminder.databinding.ActivityAddPlantBinding
import arsenic.shaw.plantwateringreminder.utils.Constants
import com.google.android.material.snackbar.Snackbar
import java.io.FileNotFoundException

class AddPlantActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPlantBinding
    private lateinit var viewModel: AddPlantViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlantBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[AddPlantViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setClickListeners()
        setObservers()
        binding.timePicker.hour

        setContentView(binding.root)
    }

    private fun setObservers() {
        viewModel.isPlantAdded.observe(this){
            if(it){
                onBackPressed()
            }
        }

        viewModel.snackBarText.observe(this){
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setClickListeners() {
        binding.close.setOnClickListener {
            onBackPressed()
        }

        binding.imagePicker.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            pickImage.launch(intent)
        }
    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->

        if(result.resultCode == RESULT_OK){
            if(result.data != null){
                val imageUri = result.data!!.data!!
                try {
                    val inputStream = contentResolver.openInputStream(imageUri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    binding.profileImage.setImageBitmap(bitmap)
                    viewModel.encodedImage = Constants.encodeImage(bitmap)

                }catch (e: FileNotFoundException){
                    e.printStackTrace()
                }
            }
        }

    }
}