// En tu LogInViewModel
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jarproductions.projecte_teapptre.repository.Repository

class LogInViewModel : ViewModel() {

    fun loginUser(email: String, password: String, context: Context): LiveData<Boolean> {
        return Repository.loginUser(email, password, context)
    }
}
