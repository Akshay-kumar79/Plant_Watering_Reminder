package arsenic.shaw.plantwateringreminder.activities.signUpActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import arsenic.shaw.plantwateringreminder.databinding.ActivitySignUpBinding
import com.google.android.material.snackbar.Snackbar

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setObservers()
        setClickListeners()

        setContentView(binding.root)
    }

    private fun setObservers() {
        viewModel.isSignedUp.observe(this){
            if (it){
                finish()
            }
        }

        viewModel.snackBarText.observe(this){
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setClickListeners() {
        binding.alreadyHaveAnAccount.setOnClickListener {
            finish()
        }
    }
}