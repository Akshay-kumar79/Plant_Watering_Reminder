package arsenic.shaw.plantwateringreminder.activities.signUpActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import arsenic.shaw.plantwateringreminder.utils.Constants
import arsenic.shaw.plantwateringreminder.utils.PreferenceManager

class SignUpViewModel(application: Application) : AndroidViewModel(application) {

    private val preferenceManager = PreferenceManager(application)

    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()

    private val _snackBarText = MutableLiveData<String>()
    val snackBarText: LiveData<String>
        get() = _snackBarText

    private val _isSignedUp = MutableLiveData<Boolean>()
    val isSignedUp: LiveData<Boolean>
        get() = _isSignedUp

    fun onSignUpClick(){
        if (isValidSignUpDetails()){
            preferenceManager.putString(Constants.USER_ID, username.value!!)
            preferenceManager.putString(Constants.PASSWORD, password.value!!)
            _isSignedUp.value = true
        }
    }

    private fun isValidSignUpDetails(): Boolean {

        if (username.value == null || username.value!!.trim().isEmpty()) {
            _snackBarText.value = "Enter username"
            return false
        } else if (password.value == null || password.value!!.trim().isEmpty()) {
            _snackBarText.value = "Enter password"
            return false
        } else if (confirmPassword.value == null || confirmPassword.value!!.trim().isEmpty()){
            _snackBarText.value = "Enter confirm password"
            return false
        } else if (confirmPassword.value != password.value){
            _snackBarText.value = "confirm password must be same as password"
            return false
        }
        return true
    }
}