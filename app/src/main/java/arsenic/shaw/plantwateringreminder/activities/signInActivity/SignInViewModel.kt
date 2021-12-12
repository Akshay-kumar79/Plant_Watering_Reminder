package arsenic.shaw.plantwateringreminder.activities.signInActivity

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import arsenic.shaw.plantwateringreminder.utils.Constants
import arsenic.shaw.plantwateringreminder.utils.PreferenceManager

class SignInViewModel(application: Application) : AndroidViewModel(application) {

    private val preferenceManager = PreferenceManager(application)

    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _snackBarText = MutableLiveData<String>()
    val snackBarText: LiveData<String>
        get() = _snackBarText

    private val _isSignedIn = MutableLiveData<Boolean>()
    val isSignedIn: LiveData<Boolean>
    get() = _isSignedIn

    init {
        _isSignedIn.value = preferenceManager.getBoolean(Constants.IS_SIGNED_IN)
    }

    fun onSignInClick(){
        if (isValidSignInDetails()){
            preferenceManager.putBoolean(Constants.IS_SIGNED_IN, true)
            _isSignedIn.value = true
        }
    }

    private fun isValidSignInDetails(): Boolean {

       if (username.value == null || username.value!!.trim().isEmpty()) {
            _snackBarText.value = "Enter username"
            return false
        } else if (password.value == null || password.value!!.trim().isEmpty()) {
            _snackBarText.value = "Enter password"
            return false
        } else if (password.value != preferenceManager.getString(Constants.PASSWORD) || username.value != preferenceManager.getString(Constants.USER_ID)){
            _snackBarText.value = "Wrong username or password"
           return false
       }
        return true
    }

}